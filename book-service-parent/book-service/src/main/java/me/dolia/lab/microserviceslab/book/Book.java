package me.dolia.lab.microserviceslab.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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