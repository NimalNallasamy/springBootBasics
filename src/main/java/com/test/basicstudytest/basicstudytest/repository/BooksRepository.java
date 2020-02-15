package com.test.basicstudytest.basicstudytest.repository;

import com.test.basicstudytest.basicstudytest.entity.AuthorDAO;
import com.test.basicstudytest.basicstudytest.entity.BooksDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<BooksDAO,Integer>, CrudRepository<BooksDAO,Integer> {

//    List<BooksDAO> saveAllBooks(List<BooksDAO> bookList);
    List<BooksDAO> findAllByBookName(String bookName);
    List<BooksDAO> findByAuthorDaoId(Integer authorDaoId);

    BooksDAO findByBookName(String bookName);
}
