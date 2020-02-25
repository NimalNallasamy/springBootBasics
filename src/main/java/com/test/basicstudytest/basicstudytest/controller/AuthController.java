package com.test.basicstudytest.basicstudytest.controller;


import com.test.basicstudytest.basicstudytest.entity.AuthProvider;
import com.test.basicstudytest.basicstudytest.entity.UserDAO;
import com.test.basicstudytest.basicstudytest.exception.BadRequestException;
import com.test.basicstudytest.basicstudytest.payload.ApiResponse;
import com.test.basicstudytest.basicstudytest.payload.AuthResponse;
import com.test.basicstudytest.basicstudytest.payload.LoginRequest;
import com.test.basicstudytest.basicstudytest.payload.SignUpRequest;
import com.test.basicstudytest.basicstudytest.repository.UserRepository;
import com.test.basicstudytest.basicstudytest.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new BadRequestException("Email address already in use.");
        }

        // Creating a user's account
        UserDAO userDAO = new UserDAO();
        userDAO.setName(signUpRequest.getName());
        userDAO.setEmail(signUpRequest.getEmail());
        userDAO.setPassword(signUpRequest.getPassword());
        userDAO.setAuthProvider(AuthProvider.local);

        UserDAO userResult = userRepository.save(userDAO);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me").buildAndExpand(userResult.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully!!"));

    }
}
