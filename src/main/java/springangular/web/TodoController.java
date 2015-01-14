package springangular.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springangular.domain.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TodoController {

    private static final AtomicLong todoIdGenerator = new AtomicLong(0);
    private static final ConcurrentSkipListMap<Long, Todo> todoRepository = new ConcurrentSkipListMap<Long, Todo>();

    static {
        Todo firstTodoTest = new Todo.Builder().withId(1L).withTitle("Test").withDescription("Description Test").build();
        Todo secondTodoTest = new Todo.Builder().withId(2L).withTitle("Secondtest").withDescription("Second description test").build();
        addTodo(firstTodoTest);
        addTodo(secondTodoTest);
    }

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<Todo> list() {
        return new ArrayList<Todo>(todoRepository.values());
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Todo getById(@PathVariable long id) {
        return todoRepository.get(id);
    }

    @RequestMapping(value = "/todo", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Todo todo) {
        long id = todoIdGenerator.incrementAndGet();
        todo.setId(id);
        todoRepository.put(id, todo);
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        todoRepository.remove(id);
    }

    private static void addTodo(Todo firstTodoTest) {
        todoRepository.put(firstTodoTest.getId(), firstTodoTest);
        todoIdGenerator.incrementAndGet();
    }
}
