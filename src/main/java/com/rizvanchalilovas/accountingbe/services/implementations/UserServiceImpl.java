package com.rizvanchalilovas.accountingbe.services.implementations;

import com.rizvanchalilovas.accountingbe.dtos.user.requests.UserRegistrationRequest;
import com.rizvanchalilovas.accountingbe.models.Status;
import com.rizvanchalilovas.accountingbe.models.User;
import com.rizvanchalilovas.accountingbe.repositories.UserJpaRepository;
import com.rizvanchalilovas.accountingbe.services.interfaces.UserService;
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
    public User register(UserRegistrationRequest request) {
        var user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );
        user.setStatus(Status.ACTIVE);

        return userRepository.saveAndFlush(user);
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
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
