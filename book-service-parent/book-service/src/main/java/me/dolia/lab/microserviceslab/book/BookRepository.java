package me.dolia.lab.microserviceslab.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Long> {
}
