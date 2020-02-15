package com.test.basicstudytest.basicstudytest.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "author")
public class AuthorDAO {

    @Id
    @GeneratedValue(generator = "author_id_generator")
    @SequenceGenerator(
            name = "author_id_generator",
            sequenceName = "author_id_seq",
            initialValue = 1
    )
    @Column(name = "id")
    private Integer id;
    @Column(name = "authorName")
    @NotNull
    private String authorName;

    @OneToMany
    @JoinColumn(name = "author_id")
    private Set<BooksDAO> books;

    /*public AuthorDAO(Integer authorId, @NotNull String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }*/

    public AuthorDAO(){
        super();

    }
    public AuthorDAO(Integer id, @NotNull String authorName) {
        this.id = id;
        this.authorName = authorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
