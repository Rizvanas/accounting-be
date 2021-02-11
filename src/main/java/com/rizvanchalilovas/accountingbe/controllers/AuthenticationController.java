package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.authentication.requests.AuthenticationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.requests.UserRegistrationRequest;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.exceptions.ApiException;
import com.rizvanchalilovas.accountingbe.models.User;
import com.rizvanchalilovas.accountingbe.repositories.UserJpaRepository;
import com.rizvanchalilovas.accountingbe.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserJpaRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserJpaRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) throws AlreadyExistsException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User with this username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email is already in use");
        }

        if (!request.getPassword().equals(request.getMatchingPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Passwords don't match");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );

        userRepository.saveAndFlush(user);

        return new ResponseEntity<>("User successfully created!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("User does not exist"));

            String token = jwtTokenProvider.createToken(request.getEmail(), String.join("; ", user.getRoles()));

            Map<Object, Object> response = new HashMap<>();
            response.put("email", request.getEmail());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
    }
}
