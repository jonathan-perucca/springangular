package springangular.web;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springangular.domain.Todo;
import springangular.repository.TodoRepository;
import springangular.web.dto.TodoDTO;
import springangular.web.exception.DataIntegrityException;
import springangular.web.exception.ErrorCode;
import springangular.web.exception.NotFoundException;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static springangular.web.exception.ErrorCode.NO_ENTITY_DELETION;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private Mapper mapper;
    
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<TodoDTO> list() {
        List<Todo> todos = from(todoRepository.findAll()).toList();
        List<TodoDTO> todoDtos = newArrayList();
        mapper.map(todos, todoDtos);
        return todoDtos;
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
    public TodoDTO getById(@PathVariable long id) {
        Todo todo = todoRepository.findOne(id);
        return mapper.map(todo, TodoDTO.class);
    }

    @RequestMapping(value = "/todo", method = RequestMethod.PUT)
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

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        try {
            todoRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(NO_ENTITY_DELETION);
        }
    }
}
