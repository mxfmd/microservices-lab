package me.dolia.lab.microserviceslab.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Book {

  @Id
  @GeneratedValue
  Long id;
  String name;
  String author;

  public static Book of(String name, String author) {
    var book = new Book();
    book.setName(name);
    book.setAuthor(author);
    return book;
  }
}