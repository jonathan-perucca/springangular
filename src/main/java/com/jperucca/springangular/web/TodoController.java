package com.jperucca.springangular.web;

import com.jperucca.springangular.domain.Todo;
import com.jperucca.springangular.repository.TodoRepository;
import com.jperucca.springangular.web.dto.TodoDTO;
import com.jperucca.springangular.web.exception.DataIntegrityException;
import com.jperucca.springangular.web.exception.ErrorCode;
import com.jperucca.springangular.web.exception.NotFoundException;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.jperucca.springangular.web.exception.ErrorCode.NO_ENTITY_FOUND;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private Mapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoDTO> list() {
        List<Todo> todos = from(todoRepository.findAll()).toList();
        List<TodoDTO> todoDtos = newArrayList();
        mapper.map(todos, todoDtos);
        return todoDtos;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TodoDTO getById(@PathVariable long id) {
        Todo todo = todoRepository.findOne(id);
        
        if(null == todo) {
            throw new NotFoundException(NO_ENTITY_FOUND);
        }
        
        return mapper.map(todo, TodoDTO.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO create(@RequestBody TodoDTO todoDTO) {
        Todo todo = mapper.map(todoDTO, Todo.class);

        Todo savedTodo;
        try {
            savedTodo = todoRepository.save(todo);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(ErrorCode.WRONG_ENTITY_INFORMATION);
        }

        return mapper.map(savedTodo, TodoDTO.class);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TodoDTO update(@PathVariable long id, @RequestBody TodoDTO todoDTO) {
        Todo todo = mapper.map(todoDTO, Todo.class);

        Todo todoToUpdate = todoRepository.findOne(id);
        
        if(null == todoToUpdate) {
            throw new NotFoundException(NO_ENTITY_FOUND);
        }
        
        todo.setId(todoToUpdate.getId());
        Todo updatedTodo = todoRepository.save(todo);

        return mapper.map(updatedTodo, TodoDTO.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        try {
            todoRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(NO_ENTITY_FOUND);
        }
    }
}
