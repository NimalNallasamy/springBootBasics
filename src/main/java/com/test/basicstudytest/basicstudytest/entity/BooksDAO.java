package com.test.basicstudytest.basicstudytest.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class BooksDAO {

    @Id
    @GeneratedValue(generator = "books_id_generator")
    @SequenceGenerator(
            name = "books_id_generator",
            sequenceName = "books_id_seq",
            initialValue = 1
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "bookName")
    @NotNull
    private String bookName;

    @Column(name="bookDescription")
    private String bookDescription;

    @Column(name = "isbn")
    @NotNull
    private String isbn;

    @Column(name = "bookPrice")
    @NotNull
    private String bookPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorDAO authorDao;

    public AuthorDAO getAuthorDao() {
        return authorDao;
    }

    public void setAuthorDao(AuthorDAO authorDao) {
        this.authorDao = authorDao;
    }

//    public void addItem(AuthorDAO authorDao) {
//        this.authorDao.add(authorDao);
//        authorDao.setOrder(this);
//    }

//    @ManyToOne()
//    @JoinColumn(name = "authorId")
//    private Integer authorId;

    //    public AuthorDAO getAuthorDetails() {
//        return authorDetails;
//    }
//
    public BooksDAO(){
        super();
    }

    public BooksDAO(AuthorDAO authorDAO, Integer id, @NotNull String bookName, String bookDescription, @NotNull String isbn, @NotNull String bookPrice, AuthorDAO authorDao) {
        this.id = id;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.isbn = isbn;
        this.bookPrice = bookPrice;
        this.authorDao = authorDao;
    }
//    public void setAuthorDetails(AuthorDAO authorDetails) {
//        this.authorDetails = authorDetails;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

}
