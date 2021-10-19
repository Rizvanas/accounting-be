package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.user.requests.UserRegistrationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.UserResponse;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.Status;
import com.rizvanchalilovas.accountingbe.models.User;
import com.rizvanchalilovas.accountingbe.repositories.UserJpaRepository;
import com.rizvanchalilovas.accountingbe.services.interfaces.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserJpaRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(UserRegistrationRequest request) throws AlreadyExistsException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User with this username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email is already in use");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );

        user = userRepository.saveAndFlush(user);

        return UserResponse.fromUser(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByUsernameStartsWith(String username) {
        return userRepository.findByUsernameContains(username);
    }

    @Override
    public UserResponse findByUsername(String username) throws NotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User could not be found"));

        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse findByEmail(String email) throws NotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User could not be found"));

        return UserResponse.fromUser(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
