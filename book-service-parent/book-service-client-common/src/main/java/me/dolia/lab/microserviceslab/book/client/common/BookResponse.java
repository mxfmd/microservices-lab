package me.dolia.lab.microserviceslab.book.client.common;

import lombok.Value;

@Value
public class BookResponse {

  String name;
  String author;
}