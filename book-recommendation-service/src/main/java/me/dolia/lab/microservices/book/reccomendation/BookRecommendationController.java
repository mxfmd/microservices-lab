package me.dolia.lab.microservices.book.reccomendation;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class BookRecommendationController {

    private final BookServiceClient bookServiceClient;

    public BookRecommendationController(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }

    @HystrixCommand(fallbackMethod = "reliable")
    @GetMapping(path = "book-recommendation")
    public Book recommendBook() {
        int id = new Random().nextInt(3) + 1;
        return bookServiceClient.getBookById((long) id).getContent();
    }

    private Book reliable() {
        Book book = new Book();
        book.setName("DEFAULT");
        return book;
    }
}
