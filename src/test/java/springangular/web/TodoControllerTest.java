package springangular.web;

import com.google.common.collect.FluentIterable;
import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springangular.SpringangularApplication;
import springangular.domain.Todo;
import springangular.repository.TodoRepository;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringangularApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:${local.http.port}")
public class TodoControllerTest {

    @Value("${local.http.port}")
    int localPort;
    
    @Autowired
    private TodoRepository todoRepository;
    
    private Todo savedTodo;
    
    @Before
    public void setUp() {
        RestAssured.port = localPort;

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
    public void should_GetAllTodos_WithTwoTestTodoInResult() {
        given()
            .log().all()
        .when()
            .get("/todo")
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("[0].id", is(savedTodo.getId().intValue()))
            .body("[0].title", is(savedTodo.getTitle()))
            .body("[0].description", is(savedTodo.getDescription()));

    }

    @Test
    public void should_GetOneTodoById_WithOneTestTodoInResult() throws Exception {
        given()
            .log().all()
        .when()
            .get("/todo/{id}", savedTodo.getId())
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("id", is(savedTodo.getId().intValue()))
            .body("title", is(savedTodo.getTitle()))
            .body("description", is(savedTodo.getDescription()));
    }

    @Test
    public void should_CreateOneTodo_Nominal() throws Exception {
        final String todoTitle = "NewTest";
        final String todoDescription = "NewDesc";
        Todo todoToCreate = new Todo.Builder().withTitle(todoTitle).withDescription(todoDescription).build();
        
        // End to end test
        
        // Start with api test
        given()
            .header("Content-Type", "application/json")
            .body(todoToCreate)
            .log().all()
        .when()
            .put("/todo")
        .then()
            .log().all()
            .statusCode(CREATED.value());

        // And then assert what has been done in db
        Todo createdTodo = todoRepository.findByTitle(todoTitle);
        
        assertThat(createdTodo, notNullValue());
        assertThat(createdTodo.getId(), notNullValue());
        assertThat(createdTodo.getDescription(), is(todoDescription));
    }
    
    @Test
    public void should_DeleteOneTodo_Nominal() {
        // End to end test
        
        // Check todos db count
        long initialTotalEntries = todoRepository.count();

        // Start with api test
        given()
            .log().all()
        .when()
            .delete("/todo/{id}", savedTodo.getId())
        .then()
            .log().all()
            .statusCode(NO_CONTENT.value());
        
        // Recheck todos db count and verify if it has well changed by -1
        long finalTotalEntries = todoRepository.count();
        assertThat(finalTotalEntries, not(initialTotalEntries));
        assertThat(finalTotalEntries, is(initialTotalEntries - 1));
    }
}