package me.dolia.lab.microserviceslab.book.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class BookServiceClientAutoConfigurationTest {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(BookServiceClientAutoConfiguration.class));

  @Test
  public void feignClientIsEnabled() {
    contextRunner.run(context -> assertThat(context).hasSingleBean(BookServiceClient.class));
  }
}