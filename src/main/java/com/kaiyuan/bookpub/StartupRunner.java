package com.kaiyuan.bookpub;

import com.kaiyuan.bookpub.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class StartupRunner implements CommandLineRunner{
    protected final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Number of books: " + bookRepository.count());
    }
}
