package me.dolia.lab.microservices.book.reccomendation;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Random;
import me.dolia.lab.microserviceslab.book.client.BookServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookRecommendationController {

  private final int maxBookId;
  private final BookServiceClient bookServiceClient;

  public BookRecommendationController(@Value("${maxBookId}") int maxBookId,
      BookServiceClient bookServiceClient) {
    this.maxBookId = maxBookId;
    this.bookServiceClient = bookServiceClient;
  }

  @CircuitBreaker(name = "bookService", fallbackMethod = "reliable")
  @GetMapping(path = "book-recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
  public Book recommendBook() {
    var id = new Random().nextInt(maxBookId) + 1;
    var response = bookServiceClient.getBookById(id);
    var book = new Book();
    book.setId((long) id);
    book.setAuthor(response.getAuthor());
    book.setName(response.getName());
    return book;
  }

  private Book reliable(Throwable e) {   //NOSONAR
    Book book = new Book();
    book.setName("DEFAULT");
    return book;
  }
}
