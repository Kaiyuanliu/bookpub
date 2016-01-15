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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

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
    private WebApplicationContext context;
    @Autowired
    private BookRepository repository;
    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;
    private RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

	@Test
	public void contextLoads() {
        assertEquals(4, repository.count());
	}

    @Test
    public void webappBookIsbnApi() {
        Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
        assertNotNull(book);
        assertEquals("Packt", book.getPublisher().getName());
    }

    @Test
    public void webappBookIsbnApiChinese() {
        Book book = restTemplate.getForObject("http://localhost:" + port + "/books/123-4678-901234", Book.class);
        assertNotNull(book);
        assertEquals("异步社区", book.getPublisher().getName());
    }

    @Test
    public void webappPublisherApi() throws Exception {
        mockMvc.perform(get("/publishers/3")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("异步社区")))
            .andExpect(jsonPath("$.name").value("异步社区"));
    }

}
