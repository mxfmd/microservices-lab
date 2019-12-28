package me.dolia.lab.microserviceslab.book.client;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.netflix.hystrix.Hystrix;
import me.dolia.lab.microserviceslab.book.client.BookServiceClientTest.BookServiceClientTestConfiguration;
import me.dolia.lab.microserviceslab.book.client.common.BookResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = "book-service.ribbon.listOfServers=http://localhost:${wiremock.server.port}",
    classes = BookServiceClientTestConfiguration.class,
    webEnvironment = WebEnvironment.NONE
)
@AutoConfigureWireMock(port = 0)
public class BookServiceClientTest {

  @Autowired
  private BookServiceClient client;

  @Before
  public void setUp() {
    WireMock.reset();
    Hystrix.reset();
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