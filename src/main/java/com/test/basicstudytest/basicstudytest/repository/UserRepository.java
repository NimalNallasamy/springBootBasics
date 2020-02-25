package com.test.basicstudytest.basicstudytest.repository;

import com.test.basicstudytest.basicstudytest.entity.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {

    Optional<UserDAO> findByEmail(String email);

    Boolean existsByEmail(String email);

}
