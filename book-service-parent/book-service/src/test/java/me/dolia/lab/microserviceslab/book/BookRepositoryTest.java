package me.dolia.lab.microserviceslab.book;

import static org.assertj.core.api.Assertions.assertThat;

import me.dolia.lab.microserviceslab.book.client.BookServiceClient;
import me.dolia.lab.microserviceslab.book.client.common.BookResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
        "eureka.client.enabled=false",
        "config.client.enabled=false",
        "book-service.ribbon.listOfServers=http://localhost:${local.server.port}",
        "feign.logging.enabled=true",
        "logging.level.me.dolia.lab.microserviceslab=trace",
        "logging.level.me.dolia.lab.microserviceslab.book.client=debug"
    },
    webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookRepositoryTest {

  @LocalServerPort
  private int port;
  @Autowired
  private BookServiceClient client;
  @Autowired
  private TestRestTemplate rest;

  @Test
  public void retrievesSingleBook() {
    var book = client.getBookById(1L);

    var expected = new BookResponse(
        "Clean Architecture: A Craftsman's Guide to Software Structure and Design",
        "Robert Cecil Martin");
    assertThat(book).isEqualTo(expected);
  }

  @Test
  public void retrieveAllBooks() {
    var url = "http://localhost:" + port + "/books";

    var booksResourceEntity = rest.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<PagedModel<Book>>() {
        });

    assertThat(booksResourceEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    var expected = new Book[]{
        Book.of("Clean Architecture: A Craftsman's Guide to Software Structure and Design",
            "Robert Cecil Martin"),
        Book.of("Cracking the Coding Interview", "Gayle Laakmann McDowell"),
        Book.of("Elon Musk: Tesla, SpaceX, and the Quest for a Fantastic Future", "Ashlee Vance")
    };
    assertThat(booksResourceEntity.getBody()).isNotNull();
    assertThat(booksResourceEntity.getBody().getContent()).containsExactly(expected);
  }
}