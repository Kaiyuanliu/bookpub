package com.kaiyuan.bookpub.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String isbn;
    private String title;
    private String description;

    @ManyToOne
    private Author author;
    @ManyToOne
    private Publisher publisher;

    @ManyToMany
    private List<Reviewer> reviewers;

    protected Book() {}

    public Book(Author author, String isbn, Publisher publisher, String title) {
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", isbn='" + isbn + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", author=" + author +
            ", publisher=" + publisher +
            ", reviewers=" + reviewers +
            '}';
    }
}
