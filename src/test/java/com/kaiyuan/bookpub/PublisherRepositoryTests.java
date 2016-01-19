package com.kaiyuan.bookpub;

import com.kaiyuan.bookpub.repository.PublisherRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BookPubApplication.class, TestMockBeansConfig.class})
@IntegrationTest
public class PublisherRepositoryTests {

    @Autowired
    private PublisherRepository repository;

    @Before
    public void setupPublisherRepositoryMock() {
        Mockito.when(repository.count()).thenReturn(1L);
    }

    @Test
    public void publishersExist() {
        assertEquals(1, repository.count());
    }

    @After
    public void resetPublisherRepositoryMock() {
        Mockito.reset(repository);
    }
}
