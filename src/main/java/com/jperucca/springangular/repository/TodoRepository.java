package com.jperucca.springangular.repository;

import org.springframework.data.repository.CrudRepository;
import com.jperucca.springangular.domain.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    Todo findByDescription(String description);
}
