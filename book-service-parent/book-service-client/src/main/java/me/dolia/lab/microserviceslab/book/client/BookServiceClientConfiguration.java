package me.dolia.lab.microserviceslab.book.client;

import feign.Logger.Level;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookServiceClientConfiguration {

  @Bean
  @ConditionalOnProperty(value = "feign.logging.enabled", havingValue = "true")
  public Level loggerLevel() {
    return Level.FULL;
  }
}