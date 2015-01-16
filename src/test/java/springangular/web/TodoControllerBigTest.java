package springangular.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import springangular.domain.Todo;
import springangular.repository.TodoRepository;
import springangular.web.dto.TodoDTO;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Big tests (or blackbox test / End to End)
 * Pushes an API call, checks that DB has expected datas
 * 
 * Api response check is done in {@link springangular.web.TodoControllerMediumTest}
 **/
public class TodoControllerBigTest extends WebAppTest{

    @Autowired
    private TodoRepository todoRepository;

    private Todo savedTodo;
    
    @Before
    public void setUp() {
        savedTodo = new Todo.Builder().withTitle("Test").withDescription("Description Test").build();
        Todo secondTodoTest = new Todo.Builder().withTitle("Secondtest").withDescription("Second description test").build();

        todoRepository.save(savedTodo);
        todoRepository.save(secondTodoTest);
    }
    
    @After
    public void tearDown() {
        todoRepository.deleteAll();
    }
    
    @Test
    public void should_Create_OneTodo_Nominal() {
        final String todoTitle = "NewTest";
        final String todoDescription = "NewDesc";
        Todo todoToCreate = new Todo.Builder().withTitle(todoTitle).withDescription(todoDescription).build();
        TodoDTO todoDTO = new TodoDTO(todoToCreate);

        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo");

        // And then assert what has been done in db
        Todo createdTodo = todoRepository.findByTitle(todoTitle);

        assertThat(createdTodo, notNullValue());
        assertThat(createdTodo.getId(), notNullValue());
        assertThat(createdTodo.getDescription(), is(todoDescription));
    }
    
    @Test
    public void should_Update_Todo_Nominal() {
        final String updatedTitle = "NewTitle of todo";
        final String updatedDescription = "NewDescription of todo";
        savedTodo.setTitle(updatedTitle);
        savedTodo.setDescription(updatedDescription);

        TodoDTO todoDTO = new TodoDTO(savedTodo);

        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo");

        Todo updatedTodo = todoRepository.findByTitle(updatedTitle);
        
        assertThat(updatedTodo, notNullValue());
        assertThat(updatedTodo.getId(), is(savedTodo.getId()));
        assertThat(updatedTodo.getDescription(), is(updatedDescription));
    }
    
    @Test
    public void should_Delete_Todo_Nominal() {
        long initialTotalEntries = todoRepository.count();

        // Start with api test
        given()
            .log().all()
        .when()
            .delete("/todo/{id}", savedTodo.getId());

        // Recheck todos db count and verify if it has well changed by -1
        long finalTotalEntries = todoRepository.count();
        assertThat(finalTotalEntries, not(initialTotalEntries));
        assertThat(finalTotalEntries, is(initialTotalEntries - 1));
    }
    
    @Test
    public void shouldNot_Delete_Todo_WhenNotFound() {
        long initialTotalEntries = todoRepository.count();

        given()
            .log().all()
        .when()
            .delete("/todo/{id}", 100);

        long finalTotalEntries = todoRepository.count();
        assertThat(finalTotalEntries, is(initialTotalEntries));
    }
}
