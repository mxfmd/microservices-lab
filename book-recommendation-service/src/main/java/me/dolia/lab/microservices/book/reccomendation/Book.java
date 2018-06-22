package me.dolia.lab.microservices.book.reccomendation;

import lombok.Data;

@Data
public class Book {

    private Long id;
    private String name;
    private String author;
}