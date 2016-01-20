package com.kaiyuan.bookpub;

import com.kaiyuan.bookpub.domain.Book;
import com.kaiyuan.bookpub.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookPubApplication.class)
@WebIntegrationTest("server.port:0")
public class BookPubApplicationTests {

    @Autowired
    private DataSource ds;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookRepository repository;
    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;
    private static boolean loadDataFixtures = true;
    private RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Before
    public void loadDataFixtures() {
        if (loadDataFixtures) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                context.getResource("classpath:/test-data.sql"));
            DatabasePopulatorUtils.execute(populator, ds);
            loadDataFixtures = false;
        }
    }

	@Test
	public void contextLoads() {
        assertEquals(3, repository.count());
	}

    @Test
    public void webappBookIsbnApi() {
        Book book = restTemplate.getForObject("http://localhost:" + port + "/books/9876-5432-1111", Book.class);
        assertNotNull(book);
        assertEquals("Packt", book.getPublisher().getName());
    }

//    @Test
//    public void webappBookIsbnApiChinese() {
//        Book book = restTemplate.getForObject("http://localhost:" + port + "/books/123-4678-901234", Book.class);
//        assertNotNull(book);
//        assertEquals("异步社区", book.getPublisher().getName());
//    }

    @Test
    public void webappPublisherApi() throws Exception {
        mockMvc.perform(get("/publishers/1")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Packt")))
            .andExpect(jsonPath("$.name").value("Packt"));
    }

}
