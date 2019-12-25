package me.dolia.lab.microserviceslab;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
        "logging.level.me.dolia.lab.microserviceslab=trace"
    },
    webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookRepositoryIT {

  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate rest;

  @Test
  public void retrievesSingleBook() {
    var url = "http://localhost:" + port + "/books/1";

    var booksResourceEntity = rest
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Book>>() {
        });

    assertThat(booksResourceEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    var expected = new Book(
        "Clean Architecture: A Craftsman's Guide to Software Structure and Design",
        "Robert Cecil Martin");
    assertThat(booksResourceEntity.getBody()).isNotNull();
    assertThat(booksResourceEntity.getBody().getContent()).isEqualTo(expected);
  }

  @Test
  public void retrieveAllBooks() {
    var url = "http://localhost:" + port + "/books";

    var booksResourceEntity = rest.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<PagedResources<Book>>() {
        });

    assertThat(booksResourceEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    var expected = new Book[]{
        new Book("Clean Architecture: A Craftsman's Guide to Software Structure and Design",
            "Robert Cecil Martin"),
        new Book("Cracking the Coding Interview", "Gayle Laakmann McDowell"),
        new Book("Elon Musk: Tesla, SpaceX, and the Quest for a Fantastic Future", "Ashlee Vance")
    };
    assertThat(booksResourceEntity.getBody()).isNotNull();
    assertThat(booksResourceEntity.getBody().getContent()).containsExactly(expected);
  }
}