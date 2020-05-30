package me.dolia.lab.microservices.book.reccomendation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("book-service")
public interface BookServiceClient {

    @GetMapping(path = "/books/{id}")
    EntityModel<Book> getBookById(@PathVariable("id") Long id);
}
