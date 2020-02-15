package com.test.basicstudytest.basicstudytest.service;

import com.test.basicstudytest.basicstudytest.dto.BooksDTO;
import com.test.basicstudytest.basicstudytest.entity.AuthorDAO;
import com.test.basicstudytest.basicstudytest.entity.BooksDAO;
import com.test.basicstudytest.basicstudytest.repository.AuthorRepository;
import com.test.basicstudytest.basicstudytest.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


//Since this is a service it does its job of converting DAO to DTO and DTO to DAO.
@Service
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private AuthorRepository authorRepository;

    // Fetch all the Data from DB So DAO to DTO
    public List<BooksDTO> getAllBooks(){

        // Fetch the list of DAO Objects
        List<BooksDAO> booksDAOList =  booksRepository.findAll();

        // List of Product DTO to be returned
        List<BooksDTO> booksDTOList = new ArrayList<BooksDTO>();

        booksDAOList.forEach( booksDAO -> {
            BooksDTO booksDTO = new BooksDTO();
            booksDTO.setBookId(booksDAO.getId());
            booksDTO.setBookName(booksDAO.getBookName());
            booksDTO.setBookDescription(booksDAO.getBookDescription());
            booksDTO.setBookPrice(booksDAO.getBookPrice());
            booksDTO.setIsbn(booksDAO.getIsbn());
            booksDTO.setAuthorName(booksDAO.getAuthorDao().getAuthorName());

            booksDTOList.add(booksDTO);
        });

        return booksDTOList;
    }

    // Fetch only a particular Book Data from DB using ID and convert the DAO to DTO.
    public  BooksDTO findParticularBookById(Integer bookId){
        BooksDAO searchedBook = booksRepository.findById(bookId).get();
        if(searchedBook != null && searchedBook.getId() != null && searchedBook.getId() > 0) {

            BooksDTO booksDTO = new BooksDTO();
            booksDTO.setBookId(searchedBook.getId());
            booksDTO.setBookName(searchedBook.getBookName());
            booksDTO.setBookDescription(searchedBook.getBookDescription());
            booksDTO.setIsbn(searchedBook.getIsbn());
            booksDTO.setBookPrice(searchedBook.getBookPrice());
            booksDTO.setAuthorName(searchedBook.getAuthorDao().getAuthorName());
            return booksDTO;
        }
        else{
            return null;
        }
    }

    // Fetch only a particular Data from DB and Convert the fetched DAO into DTO
    public BooksDTO findParticularBookByBookName(String bookName){

        BooksDAO searchedBook = booksRepository.findByBookName(bookName);
        if(searchedBook != null && searchedBook.getId() != null && searchedBook.getId() > 0) {

            BooksDTO booksDTO = new BooksDTO();
            booksDTO.setBookId(searchedBook.getId());
            booksDTO.setBookName(searchedBook.getBookName());
            booksDTO.setBookDescription(searchedBook.getBookDescription());
            booksDTO.setIsbn(searchedBook.getIsbn());
            booksDTO.setBookPrice(searchedBook.getBookPrice());
            booksDTO.setAuthorName(searchedBook.getAuthorDao().getAuthorName());
            return booksDTO;
        }
        else{
            return null;
        }
    }

   /* // Fetch only a particular Data from DB and Convert the fetched DAO into DTO
    public BooksDTO findParticularBookByAuthor(String author){

        BooksDAO searchedBook = booksRepository.findByAuthor(author);

        BooksDTO booksDTO = new BooksDTO();
        booksDTO.setBookId(searchedBook.getBookId());
        booksDTO.setBookName(searchedBook.getBookName());
        booksDTO.setBookDescription(searchedBook.getBookDescription());
        booksDTO.setIsbn(searchedBook.getIsbn());
        booksDTO.setBookPrice(searchedBook.getBookPrice());
        booksDTO.setAuthor(searchedBook.getAuthor());
        return booksDTO;
    }*/

    // Fetch all books by the author
    public List<BooksDTO> findAllBookByAuthor(String author){

        AuthorDAO authorDAO = authorRepository.findByAuthorName(author);

        List<BooksDAO> searchedBookDAO = booksRepository.findByAuthorDaoId(authorDAO.getId());

        List<BooksDTO> searchedBookDTO = new ArrayList<>();

        searchedBookDAO.forEach( searchedBook -> {
            BooksDTO booksDTO = new BooksDTO();
            booksDTO.setBookId(searchedBook.getId());
            booksDTO.setBookName(searchedBook.getBookName());
            booksDTO.setBookDescription(searchedBook.getBookDescription());
            booksDTO.setIsbn(searchedBook.getIsbn());
            booksDTO.setBookPrice(searchedBook.getBookPrice());
            booksDTO.setAuthorName(searchedBook.getAuthorDao().getAuthorName());
            searchedBookDTO.add(booksDTO);
        });

        return searchedBookDTO;
    }

    // Convert the data to DB
    public List<BooksDTO> addBooks(List<BooksDTO> booksDTO){

        List<BooksDAO> daoList = new ArrayList<>();
        List<BooksDTO> dtoList = new ArrayList<>();

        //Convert Books DTO to DAO
        Iterator iterator = booksDTO.iterator();
        while(iterator.hasNext()){
            BooksDTO bookDTO = (BooksDTO)iterator.next();
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.setId(bookDTO.getBookId());
            booksDAO.setBookName(bookDTO.getBookName());
            booksDAO.setBookDescription(bookDTO.getBookDescription());
            booksDAO.setIsbn(bookDTO.getIsbn());
            booksDAO.setBookPrice(bookDTO.getBookPrice());

            AuthorDAO authorDAO = authorRepository.findByAuthorName(bookDTO.getAuthorName());
            if(authorDAO != null && authorDAO.getId() > 0){
                booksDAO.setAuthorDao(authorDAO);
            }
            else{
                authorDAO = new AuthorDAO();
                authorDAO.setAuthorName(bookDTO.getAuthorName());
                booksDAO.setAuthorDao(authorDAO);
                authorRepository.save(authorDAO);
            }

            daoList.add(booksDAO);
        }

        // Save the DAO to Database
        List<BooksDAO> daoList1 = booksRepository.saveAll(daoList);

        // Convert the saved DAO to DTO
        daoList1.forEach( savedBooksDAO -> {
            BooksDTO savedBooksDTO = new BooksDTO();
            savedBooksDTO.setBookId(savedBooksDAO.getId());
            savedBooksDTO.setBookName(savedBooksDAO.getBookName());
            savedBooksDTO.setBookDescription(savedBooksDAO.getBookDescription());
            savedBooksDTO.setIsbn(savedBooksDAO.getIsbn());
            savedBooksDTO.setBookPrice(savedBooksDAO.getBookPrice());
            savedBooksDTO.setAuthorName(savedBooksDAO.getAuthorDao().getAuthorName());

            dtoList.add(savedBooksDTO);
        });

        return dtoList;
    }

    // Delete the book details based on the book Id provided
    public boolean deleteBookById(Integer bookId){
        try {
            booksRepository.deleteById(bookId);
        }
        catch (Exception ex){
            System.out.println("Exception caused by ::: "+ex);
            return false;
        }
        return true;
    }
}
