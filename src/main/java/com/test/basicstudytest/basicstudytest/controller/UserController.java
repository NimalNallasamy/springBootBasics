package com.test.basicstudytest.basicstudytest.controller;

import com.test.basicstudytest.basicstudytest.entity.UserDAO;
import com.test.basicstudytest.basicstudytest.exception.ResourceNotFoundException;
import com.test.basicstudytest.basicstudytest.repository.UserRepository;
import com.test.basicstudytest.basicstudytest.security.CurrentUser;
import com.test.basicstudytest.basicstudytest.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public UserDAO getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        return userRepository.findById(userPrincipal.getId()).orElseThrow(()-> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
