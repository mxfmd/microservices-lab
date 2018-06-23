package me.dolia.lab.microserviceslab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class BookServiceApplication {

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeBookStorage() {
		return args -> {
			bookRepository.save(new Book("Clean Architecture: A Craftsman's Guide to Software Structure and Design", "Robert Cecil Martin"));
			bookRepository.save(new Book("Cracking the Coding Interview", "Gayle Laakmann McDowell"));
			bookRepository.save(new Book("Elon Musk: Tesla, SpaceX, and the Quest for a Fantastic Future", "Ashlee Vance"));
		};
	}
}
