import com.kaiyuan.bookpub.BookPubApplication
import com.kaiyuan.bookpub.TestMockBeansConfig
import com.kaiyuan.bookpub.domain.Author
import com.kaiyuan.bookpub.domain.Book
import com.kaiyuan.bookpub.domain.Publisher
import com.kaiyuan.bookpub.repository.BookRepository
import com.kaiyuan.bookpub.repository.PublisherRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = [BookPubApplication.class, TestMockBeansConfig.class],
                        loader = SpringApplicationContextLoader.class)
class SpockBookRepositorySpec extends Specification {
    @Autowired
    private ConfigurableApplicationContext context;
    @Shared
    boolean sharedSetupDone = false;
    @Autowired
    private DataSource ds;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Shared
    private MockMvc mockMvc;

    void setup() {
        if(!sharedSetupDone) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
            sharedSetupDone = true;
        }

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                context.getResource("classpath:/packt-books.sql"));

        DatabasePopulatorUtils.execute(populator, ds);
    }

    @Transactional
    def "Test RESTful GET"() {
        when:
        def result = mockMvc.perform(get("/books/${isbn}"));

        then:
        result.andExpect(status().isOk());
        result.andExpect(content().string(containsString(title)));

        where:
        isbn                | title
        "978-1-78398-478-7" |"Orchestrating Docker"
        "9876-5432-1111"    |"Spring Boot Recipes"
    }

    @Transactional
    def "Insert another book"() {
        setup:
        def existingBook = bookRepository.findBookByIsbn("9876-5432-1111");
        def newBook = new Book(existingBook.getAuthor(), "978-1-12345-678-9",
                existingBook.getPublisher(), "Some Future Book");

        expect:
        bookRepository.count() == 3

        when:
        def saveBook = bookRepository.save(newBook);

        then:
        bookRepository.count() == 4
        saveBook.id > -1
    }

    def "Test RESTful GET books by publisher"() {
        setup:
        Publisher publisher = new Publisher("Strange Books")
        publisher.setId(999)
        Book book = new Book(new Author("John", "Done"), "978-1-98765-432-1", publisher, "Mytery Book")
        publisher.setBooks([book])
        Mockito.when(publisherRepository.count()).thenReturn(1L)
        Mockito.when(publisherRepository.findOne(1L)).thenReturn(publisher)

        when:
        def result = mockMvc.perform(get("/books/publisher/1"))

        then:
        result.andExpect(status().isOk())
        result.andExpect(content().string(containsString("Strange Books")))

        cleanup:
        Mockito.reset(publisherRepository)
    }


}
