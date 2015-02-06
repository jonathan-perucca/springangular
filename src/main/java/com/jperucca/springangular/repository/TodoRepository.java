package com.jperucca.springangular.repository;

import org.springframework.data.repository.CrudRepository;
import com.jperucca.springangular.domain.Todo;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    Todo findByDescription(String description);

    List<Todo> findByChecked(boolean checked);
}
