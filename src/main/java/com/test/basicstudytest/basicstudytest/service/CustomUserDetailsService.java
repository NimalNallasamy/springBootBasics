package com.test.basicstudytest.basicstudytest.service;


import com.test.basicstudytest.basicstudytest.entity.UserDAO;
import com.test.basicstudytest.basicstudytest.exception.ResourceNotFoundException;
import com.test.basicstudytest.basicstudytest.repository.UserRepository;
import com.test.basicstudytest.basicstudytest.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDAO userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with the Email :: "+email));

        return UserPrincipal.create(userDetails);
    }

    @Transactional
    public UserDetails loadUserById(Long id){
        UserDAO userDAO = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User","id",id)
        );

        return UserPrincipal.create(userDAO);
    }
}
