package me.dolia.lab.microservices.book.reccomendation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = {
        "eureka.client.enabled=false",
        "config.client.enabled=false",
        "maxBookId=2"
    },
    webEnvironment = WebEnvironment.NONE
)
public class BookRecommendationApplicationTest {

  @Autowired
  private ApplicationContext context;

  @Test
  public void contextLoads() {
    assertThat(context).isNotNull();
  }
}