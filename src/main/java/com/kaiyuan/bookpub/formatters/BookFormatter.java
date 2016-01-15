package com.kaiyuan.bookpub.formatters;

import com.kaiyuan.bookpub.domain.Book;
import com.kaiyuan.bookpub.repository.BookRepository;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class BookFormatter implements Formatter<Book> {
    private BookRepository bookRepository;

    public BookFormatter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book parse(String bookIdentifer, Locale locale) throws ParseException {
        System.out.println("what");
        Book book = bookRepository.findBookByIsbn(bookIdentifer);
        return (book != null) ? book : bookRepository.findOne(Long.valueOf(bookIdentifer));
    }

    @Override
    public String print(Book book, Locale locale) {
        return book.getIsbn();
    }
}
