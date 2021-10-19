package com.rizvanchalilovas.accountingbe.services.interfaces;

import com.rizvanchalilovas.accountingbe.dtos.user.requests.UserRegistrationRequest;
import com.rizvanchalilovas.accountingbe.dtos.user.responses.UserResponse;
import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.models.User;
import javassist.NotFoundException;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationRequest request) throws AlreadyExistsException;
    List<User> getAll();
    List<User> findByUsernameStartsWith(String username);
    UserResponse findByUsername(String username) throws NotFoundException;
    UserResponse findByEmail(String email) throws NotFoundException;
    User findById(Long id);
    void deleteById(Long id);
}
