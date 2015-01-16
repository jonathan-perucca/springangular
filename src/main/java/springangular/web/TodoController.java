package springangular.web;

import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springangular.domain.Todo;
import springangular.repository.TodoRepository;
import springangular.web.exception.NotFoundException;

import java.util.List;

import static springangular.web.exception.ErrorCode.NO_ENTITY_DELETION;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<Todo> list() {
        return FluentIterable.from(todoRepository.findAll()).toList();
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
    public Todo getById(@PathVariable long id) {
        return todoRepository.findOne(id);
    }

    @RequestMapping(value = "/todo", method = RequestMethod.PUT)
    public Todo create(@RequestBody Todo todo) {
        return todoRepository.save(todo);
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
