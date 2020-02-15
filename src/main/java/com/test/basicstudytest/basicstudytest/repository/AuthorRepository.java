package com.test.basicstudytest.basicstudytest.repository;

import com.test.basicstudytest.basicstudytest.entity.AuthorDAO;
import com.test.basicstudytest.basicstudytest.entity.BooksDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorDAO, Integer>, CrudRepository<AuthorDAO,Integer> {

    AuthorDAO findByAuthorName(String authorName);
}
