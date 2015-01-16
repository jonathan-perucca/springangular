package springangular.web;

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
import springangular.web.dto.TodoDTO;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static springangular.web.exception.ErrorCode.NO_ENTITY_DELETION;

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
    public void should_Get_AllTodos_WithTwoTestTodoInResult() {
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
    public void should_Get_OneTodoById_WithOneTestTodoInResult() throws Exception {
        given()
            .log().all()
        .when()
            .get("/todo/{id}", savedTodo.getId())
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(savedTodo.getId().intValue()))
            .body("todo.title", is(savedTodo.getTitle()))
            .body("todo.description", is(savedTodo.getDescription()));
    }

    @Test
    public void should_Create_OneTodo_Nominal() throws Exception {
        final String todoTitle = "NewTest";
        final String todoDescription = "NewDesc";
        Todo todoToCreate = new Todo.Builder().withTitle(todoTitle).withDescription(todoDescription).build();
        TodoDTO todoDTO = new TodoDTO(todoToCreate);
        
        // TODO :: To split in 2 differents tests (E2E / Api test)
        // End to end test
        
        // Start with api test
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo")
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", notNullValue())
            .body("todo.title", is(todoTitle))
            .body("todo.description", is(todoDescription));

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

        // TODO :: To split in 2 differents tests (E2E / Api test)
        
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo")
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(savedTodo.getId().intValue()))
            .body("todo.title", is(updatedTitle))
            .body("todo.description", is(updatedDescription));
    }
    
    @Test
    public void should_Delete_OneTodo_Nominal() {
        // TODO :: To split in 2 differents tests (E2E / Api test)
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
    
    @Test
    public void shouldNot_Delete_OneTodo_WhenNotFound() {
        long initialTotalEntries = todoRepository.count();

        // TODO :: To split in 2 differents tests (E2E / Api test)
        given()
            .log().all()
        .when()
            .delete("/todo/{id}", 100)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value())
            .body("url", is("/todo/100"))
            .body("errorCode", is(NO_ENTITY_DELETION.getCode()))
            .body("reasonCause", is(NO_ENTITY_DELETION.getDescription()));

        long finalTotalEntries = todoRepository.count();
        assertThat(finalTotalEntries, is(initialTotalEntries));
    }
}