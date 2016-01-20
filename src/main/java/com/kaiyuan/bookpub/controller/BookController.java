package com.kaiyuan.bookpub.controller;


import com.kaiyuan.bookpub.domain.Book;
import com.kaiyuan.bookpub.domain.Publisher;
import com.kaiyuan.bookpub.domain.Reviewer;
import com.kaiyuan.bookpub.repository.AuthorRepository;
import com.kaiyuan.bookpub.repository.BookRepository;
import com.kaiyuan.bookpub.repository.PublisherRepository;
import com.kaiyuan.bookpub.utils.Isbn;
import com.kaiyuan.bookpub.utils.IsbnEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public Book getBook(@PathVariable Isbn isbn) {
        return bookRepository.findBookByIsbn(isbn.getIsbn());
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteBook(@PathVariable String isbn) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            bookRepository.deleteBookByIsbn(isbn);
        } catch (NullPointerException e) {
            logger.error("the book is not in database");
            response.put("message", "delete failure");
            response.put("code", 0);
        }

        response.put("message", "delete successfully");
        response.put("code", 1);
        return response;
    }

    @RequestMapping(value = "/{isbn}/{title}", method = RequestMethod.PUT)
    public Map<String, Object> updateBookTitle(@PathVariable String isbn, @PathVariable String title) {
        Map<String, Object> response = new LinkedHashMap<>();
        Book book;
        try {
            book = bookRepository.findBookByIsbn(isbn);
            book.setTitle(title);
            bookRepository.save(book);
        } catch (NullPointerException e) {
            response.put("message", "can not find the book");
            return response;
        }

        response.put("message", "book update successfully");
        response.put("book", book);
        return response;
    }

    @RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
    public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
        return book.getReviewers();
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @RequestMapping(value = "/publisher/{id}", method = RequestMethod.GET)
    public List<Book> getBooksByPublisher(@PathVariable("id") Long id) {
        Publisher publisher = publisherRepository.findOne(id);
        Assert.notNull(publisher);
        return publisher.getBooks();
    }
}
