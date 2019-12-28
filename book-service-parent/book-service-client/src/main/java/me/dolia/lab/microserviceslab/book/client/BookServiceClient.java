package me.dolia.lab.microserviceslab.book.client;

import me.dolia.lab.microserviceslab.book.client.common.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("book-service")
public interface BookServiceClient {

  @GetMapping(path = "/books/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  Resource<BookResponse> getBookById(@PathVariable("id") long id);
}