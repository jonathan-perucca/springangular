package com.jperucca.springangular.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.jperucca.springangular.domain.Todo;
import com.jperucca.springangular.repository.TodoRepository;
import com.jperucca.springangular.web.dto.TodoDTO;

import static com.jayway.restassured.RestAssured.given;
import static com.jperucca.springangular.domain.Todo.newTodo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static com.jperucca.springangular.web.exception.ErrorCode.NO_ENTITY_FOUND;
import static com.jperucca.springangular.web.exception.ErrorCode.WRONG_ENTITY_INFORMATION;

/**
 * Local big tests (or blackbox test / End to End)
 * Pushes an API call, checks that DB has expected datas
 * 
 **/
public class TodoControllerBigTest extends WebAppTest {

    @Autowired
    private TodoRepository todoRepository;

    private Todo savedTodo;
    
    @Before
    public void setUp() {
        savedTodo = newTodo().withDescription("Description Test").build();
        Todo secondTodoTest = newTodo().withDescription("Second description test").build();

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
            .body("[0].description", is(savedTodo.getDescription()));
    }

    @Test
    public void should_Get_OneTodoById_WithOneTestTodoInResult() {
        given()
            .log().all()
        .when()
            .get("/todo/{id}", savedTodo.getId())
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(savedTodo.getId().intValue()))
            .body("todo.description", is(savedTodo.getDescription()));
    }
    
    @Test
    public void shouldNot_Get_OneTodoById_WhenNotFound() {
        final int unknownId = 100;
        given()
            .log().all()
        .when()
            .get("/todo/{id}", unknownId)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value())
            .body("url", is("/todo/" + unknownId))
            .body("errorCode", is(NO_ENTITY_FOUND.getCode()))
            .body("reasonCause", is(NO_ENTITY_FOUND.getDescription()));
    }
    
    @Test
    public void should_Create_OneTodo_Nominal() {
        final String todoDescription = "NewDesc";
        Todo todoToCreate = newTodo().withDescription(todoDescription).build();
        TodoDTO todoDTO = new TodoDTO(todoToCreate);

        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .post("/todo")
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("todo.id", notNullValue())
            .body("todo.description", is(todoDescription));

        // And then assert what has been done in db
        Todo createdTodo = todoRepository.findByDescription(todoDescription);

        assertThat(createdTodo, notNullValue());
        assertThat(createdTodo.getId(), notNullValue());
        assertThat(createdTodo.getDescription(), is(todoDescription));
    }
    
    @Test
    public void shouldNot_Create_Todo_WhenNoDescription() {
        TodoDTO todoDTO = new TodoDTO(newTodo().build());
        
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .post("/todo")
        .then()
            .statusCode(BAD_REQUEST.value())
            .log().all()
            .body("url", is("/todo"))
            .body("errorCode", is(WRONG_ENTITY_INFORMATION.getCode()))
            .body("reasonCause", is(WRONG_ENTITY_INFORMATION.getDescription()));
    }
    
    @Test
    public void should_Update_Todo_Nominal() {
        Long savedTodoId = savedTodo.getId();
        final String updatedDescription = "NewDescription of todo";

        TodoDTO todoDTO = new TodoDTO(newTodo().withDescription(updatedDescription).build());
        
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo/{id}", savedTodoId)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(savedTodo.getId().intValue()))
            .body("todo.description", is(updatedDescription));

        Todo updatedTodo = todoRepository.findByDescription(updatedDescription);

        assertThat(updatedTodo, notNullValue());
        assertThat(updatedTodo.getId(), is(savedTodo.getId()));
        assertThat(updatedTodo.getDescription(), is(updatedDescription));
    }
    
    @Test
    public void shouldNot_Update_Todo_WhenTodoNotFound() {
        Long unknownTodoId = 100L;
        final String updatedDescription = "NewDescription of todo";

        TodoDTO todoDTO = new TodoDTO(newTodo().withDescription(updatedDescription).build());
        
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo/{id}", unknownTodoId)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value())
            .body("url", is("/todo/100"))
            .body("errorCode", is(NO_ENTITY_FOUND.getCode()))
            .body("reasonCause", is(NO_ENTITY_FOUND.getDescription()));
    }
    
    @Test
    public void should_Delete_Todo_Nominal() {
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
    public void shouldNot_Delete_Todo_WhenNotFound() {
        long initialTotalEntries = todoRepository.count();

        given()
            .log().all()
        .when()
            .delete("/todo/{id}", 100)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value())
            .body("url", is("/todo/100"))
            .body("errorCode", is(NO_ENTITY_FOUND.getCode()))
            .body("reasonCause", is(NO_ENTITY_FOUND.getDescription()));

        long finalTotalEntries = todoRepository.count();
        assertThat(finalTotalEntries, is(initialTotalEntries));
    }
}
