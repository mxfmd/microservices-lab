package me.dolia.lab.microserviceslab.book.client;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = BookServiceClient.class)
@AutoConfigureAfter(FeignRibbonClientAutoConfiguration.class)
public class BookServiceClientAutoConfiguration {

}