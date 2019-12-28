package me.dolia.lab.microserviceslab.book.client;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = BookServiceClient.class)
@AutoConfigureAfter(RibbonAutoConfiguration.class)
public class BookServiceClientAutoConfiguration {

}