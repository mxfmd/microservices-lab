package me.dolia.lab.microserviceslab.book.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;

@AutoConfiguration
@EnableFeignClients(clients = BookServiceClient.class)
@AutoConfigureAfter(FeignLoadBalancerAutoConfiguration.class)
public class BookServiceClientAutoConfiguration {

}