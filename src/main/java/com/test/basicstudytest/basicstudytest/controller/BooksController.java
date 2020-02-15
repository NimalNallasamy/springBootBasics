package com.test.basicstudytest.basicstudytest.controller;

import com.test.basicstudytest.basicstudytest.dto.BooksDTO;
import com.test.basicstudytest.basicstudytest.entity.BooksDAO;
import com.test.basicstudytest.basicstudytest.service.BooksService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BooksController {

    List<BooksDTO> booksList = new ArrayList<BooksDTO>();

    @Autowired
    public BooksService booksService;

//    @PostConstruct
//    public void dataInit(){
//
//        // Hard Coding Book Details in the server
//        // Book 1
//        BooksDTO booksDTO = new BooksDTO();
//
//        booksDTO.setBookId(1);
//        booksDTO.setBookName("Angels and Demons");
//        booksDTO.setBookDescription("Angels & Demons is a 2000 bestselling mystery-thriller novel written by American author Dan Brown and published by Pocket Books and then by Corgi Books. The novel introduces the character Robert Langdon, who recurs as the protagonist of Brown's subsequent novels.");
//        booksDTO.setBookPrice("200INR");
//        booksDTO.setIsbn("ISBN1");
//        booksDTO.setAuthor("Dan Brown");
//        booksList.add(booksDTO);
//
//        // Book 2
//        booksDTO = new BooksDTO();
//        booksDTO.setBookId(2);
//        booksDTO.setBookName("Wings of Fire");
//        booksDTO.setBookDescription("An Autobiography of A P J Abdul Kalam (1999), former President of India. It was written by Dr. Kalam and Arun Tiwari. Kalam examines his early life, effort, hardship, fortitude, luck and chance that eventually led him to lead Indian space research, nuclear and missile programs.");
//        booksDTO.setBookPrice("250INR");
//        booksDTO.setIsbn("ISBN2");
//        booksDTO.setAuthor("Dr. APJ Abdul Kalam");
//        booksList.add(booksDTO);
//    }

    @GetMapping("/books")
    public List listAllBooks(){

        return booksService.getAllBooks();
//        return booksList;
    }

    @GetMapping(value = "/books",params = "bookName")
    public BooksDTO searchBookByBookName(@RequestParam String bookName){

        // Old Mehtod without DB inclusion
//        List<BooksDTO> searchedBooksList = new ArrayList<BooksDTO>();
//        booksList.forEach(booksDTO -> {
//            if(booksDTO.getBookName().equals(bookName)){
//                searchedBooksList.add(booksDTO);
//            }
//        });
//        return searchedBooksList;

        // New Method with DB Inclusion
        BooksDTO bookList = new BooksDTO();
        if(bookName != null && bookName.length() > 0) {
            bookList = booksService.findParticularBookByBookName(bookName);
            if(bookList != null){
                return bookList;
            }
        }
        return bookList;
    }

    @GetMapping(value = "/books/{bookId}")
    public BooksDTO searchBookById(@PathVariable("bookId") Integer bookId){
        BooksDTO bookList =  new BooksDTO();
        if(bookId != null && bookId > 0) {
            bookList = booksService.findParticularBookById(bookId);
            if(bookList != null){
                return bookList;
            }
        }
        return bookList;
    }

    @GetMapping(value = "/books",params = "authorName")
    public List searchBookByAuthor(@RequestParam String authorName){
        List<BooksDTO> bookList = new ArrayList<>();
        if(authorName != null && authorName.length() > 0) {
            bookList = booksService.findAllBookByAuthor(authorName);
            if(bookList != null){
                return bookList;
            }
        }
        return bookList;
    }

    @PostMapping("/books")
    public String addBook(@RequestBody BooksDTO booksDTO){
        // Old Code without DB involvement
//        booksList.add(booksDTO);
//        return "Record added successfully";

        // New Code with DB Involvement

        List<BooksDTO> savedBooksDTO = new ArrayList<>();
        savedBooksDTO.add(booksDTO);
        savedBooksDTO = booksService.addBooks(savedBooksDTO);
        if(savedBooksDTO != null  && savedBooksDTO.size() > 0) {
            return "Books Added Successfully";
        }
        else{
            return "Books Addition Unsuccessful";
        }
    }

    /*@PostMapping("/books")
    public String addBooks(@RequestBody List<BooksDTO> booksDTO){
        // Old Code without DB involvement
//        booksList.add(booksDTO);
//        return "Record added successfully";

        // New Code with DB Involvement
        List<BooksDTO> savedBooksDTO = booksService.addBooks(booksDTO);
        if(savedBooksDTO != null && savedBooksDTO.size() > 0) {
            return "Books Added Successfully";
        }
        else{
            return "Books Addition Unsuccessful";
        }
    }*/

    @DeleteMapping(path = "/books/{bookId}")
    public String deleteBooks(@PathVariable("bookId") Integer bookId){
        // Old code without DB Handling
       /* List<BooksDTO> toBeDeletedBooksList = new ArrayList<BooksDTO>();
        try {
            booksList.forEach(booksDTO -> {
                if (bookId != null && booksDTO.getBookId().equals(bookId)) {
                    toBeDeletedBooksList.add(booksDTO);
                }
            });
        }
        catch(Exception ex){
            return "Provide the bookId correctly";
        }
        if(toBeDeletedBooksList.isEmpty()) {
            return "No Such Records Found";
        }
        booksList.removeAll(toBeDeletedBooksList);
        return "Records Deleted successfully";*/

       // New Code with DB handing
        if(bookId != null && bookId > 0) {
            boolean success = booksService.deleteBookById(bookId);
            if(success){
                return "Records Deleted successfully";
            }
            else{
                return "Provide the bookId correctly";
            }
        }

        return "Invalid BookId kindly provide the correct bookId";
    }

}
