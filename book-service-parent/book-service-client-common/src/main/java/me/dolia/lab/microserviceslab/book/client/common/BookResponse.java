package me.dolia.lab.microserviceslab.book.client.common;

import lombok.Value;

@Value
public class BookResponse {

  long id;
  String name;
  String author;
}