package me.dolia.lab.microservices.book.reccomendation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Random;

@RestController
public class BookRecommendationController {

    private final RestTemplate restTemplate;

    public BookRecommendationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "book-recommendation")
    public Book recommendBook() {
        int id = new Random().nextInt(3) + 1;
        ResponseEntity<Resource<Book>> entity = restTemplate.exchange("http://book-service/books/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Book>>() {
        }, Collections.emptyMap());
        return entity.getBody().getContent();
    }
}
