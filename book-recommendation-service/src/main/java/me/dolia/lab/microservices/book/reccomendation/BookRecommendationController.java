package me.dolia.lab.microservices.book.reccomendation;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class BookRecommendationController {

    private final int maxBookId;
    private final BookServiceClient bookServiceClient;

    public BookRecommendationController(@Value("${maxBookId}") int maxBookId, BookServiceClient bookServiceClient) {
        this.maxBookId = maxBookId;
        this.bookServiceClient = bookServiceClient;
    }

    @HystrixCommand(fallbackMethod = "reliable")
    @GetMapping(path = "book-recommendation")
    public Book recommendBook() {
        int id = new Random().nextInt(maxBookId) + 1;
        return bookServiceClient.getBookById((long) id).getContent();
    }

    private Book reliable() {   //NOSONAR
        Book book = new Book();
        book.setName("DEFAULT");
        return book;
    }
}
