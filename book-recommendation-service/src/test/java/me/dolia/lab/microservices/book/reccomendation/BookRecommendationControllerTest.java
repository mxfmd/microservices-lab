package me.dolia.lab.microservices.book.reccomendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import me.dolia.lab.microserviceslab.book.client.BookServiceClient;
import me.dolia.lab.microserviceslab.book.client.BookServiceClientAutoConfiguration;
import me.dolia.lab.microserviceslab.book.client.common.BookResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
        "eureka.client.enabled=false",
        "config.client.enabled=false",
        "maxBookId=2"
    },
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@ImportAutoConfiguration(exclude = BookServiceClientAutoConfiguration.class)
public class BookRecommendationControllerTest {

  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate rest;
  @MockBean
  private BookServiceClient bookService;

  @Test
  public void recommendBook() {
    var url = "http://localhost:" + port + "/book-recommendation";
    var bookResponse = new BookResponse(
        "Clean Architecture: A Craftsman's Guide to Software Structure and Design",
        "Robert Cecil Martin");
    when(bookService.getBookById(anyLong())).thenReturn(bookResponse);

    var book = rest.getForObject(url, Book.class);

    assertThat(book).isEqualToComparingOnlyGivenFields(bookResponse, "name", "author")
        .extracting(Book::getId)
        .isNotNull();
  }
}