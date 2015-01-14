package springangular.web;

import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springangular.domain.Todo;
import springangular.repository.TodoRepository;

import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<Todo> list() {
        return FluentIterable.from(todoRepository.findAll()).toList();
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Todo getById(@PathVariable long id) {
        return todoRepository.findOne(id);
    }

    @RequestMapping(value = "/todo", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Todo todo) {
        todoRepository.save(todo);
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        todoRepository.delete(id);
    }
}
