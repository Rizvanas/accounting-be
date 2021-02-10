package com.rizvanchalilovas.accountingbe.controllers;

import com.rizvanchalilovas.accountingbe.dtos.user.responses.UserResponse;
import com.rizvanchalilovas.accountingbe.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        var user = userService.findById(id);

        return new ResponseEntity<>(UserResponse.fromUser(user), HttpStatus.OK);
    }
}
