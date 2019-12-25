package me.dolia.lab.microserviceslab.book;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String author;

    public Book() {
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
}
