package com.rizvanchalilovas.accountingbe.services.interfaces;

import com.rizvanchalilovas.accountingbe.dtos.user.requests.UserRegistrationRequest;
import com.rizvanchalilovas.accountingbe.models.User;

import java.util.List;

public interface UserService {
    User register(UserRegistrationRequest request);
    List<User> getAll();
    List<User> findByUsernameStartsWith(String username);
    User findById(Long id);
    void deleteById(Long id);
}
