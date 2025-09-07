package me.dolia.lab.microserviceslab.book.client;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import me.dolia.lab.microserviceslab.book.client.BookServiceClientTest.BookServiceClientTestConfiguration;
import me.dolia.lab.microserviceslab.book.client.common.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest(
    properties = {
        "spring.cloud.discovery.client.simple.instances.book-service[0].uri=http://localhost:${wiremock.server.port}",
        "feign.logging.enabled=true",
        "logging.level.me.dolia.lab.microserviceslab.book.client=debug"
    },
    classes = BookServiceClientTestConfiguration.class,
    webEnvironment = WebEnvironment.NONE
)
@EnableWireMock
public class BookServiceClientTest {

  @Autowired
  private BookServiceClient client;

  @BeforeEach
  public void setUp() {
    WireMock.reset();
  }

  @Test
  public void getBookById() {
    stubFor(get(urlEqualTo("/books/1"))
        .willReturn(okJson("{\"name\":\"Name\",\"author\":\"Some author\"}")));
    var expected = new BookResponse("Name", "Some author");

    var response = client.getBookById(1);

    assertThat(response).isEqualTo(expected);
  }

  @Configuration
  @EnableAutoConfiguration
  static class BookServiceClientTestConfiguration {

  }
}