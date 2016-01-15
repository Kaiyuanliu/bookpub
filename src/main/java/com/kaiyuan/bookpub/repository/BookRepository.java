package com.kaiyuan.bookpub.repository;

import com.kaiyuan.bookpub.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    Book findBookByIsbn(String isbn);
    void deleteBookByIsbn(String isbn);
}
