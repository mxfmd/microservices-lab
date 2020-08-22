package me.dolia.lab.microservices.book.reccomendation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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