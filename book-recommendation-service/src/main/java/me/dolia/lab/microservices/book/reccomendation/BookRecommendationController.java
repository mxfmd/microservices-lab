package me.dolia.lab.microservices.book.reccomendation;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
public class BookRecommendationController {

    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public BookRecommendationController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping(path = "book-recommendation")
    public Book recommendBook() {
        int id = new Random().nextInt(3) + 1;
        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
        String url;
        if (!(instances == null || instances.isEmpty())) {
            url = instances.get(0).getUri().toString();
        } else {
            url = "http://book-service:8081";
        }
        ResponseEntity<Resource<Book>> entity = restTemplate.exchange(url + "/books/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Book>>() {
        }, Collections.emptyMap());
        return entity.getBody().getContent();
    }
}
