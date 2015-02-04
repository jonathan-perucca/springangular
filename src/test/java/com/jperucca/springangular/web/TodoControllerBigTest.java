package com.jperucca.springangular.web;

import com.jperucca.springangular.domain.Todo;
import com.jperucca.springangular.repository.TodoRepository;
import com.jperucca.springangular.web.dto.TodoDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static com.jayway.restassured.RestAssured.given;
import static com.jperucca.springangular.domain.Todo.newTodo;
import static com.jperucca.springangular.web.exception.ErrorCode.NO_ENTITY_FOUND;
import static com.jperucca.springangular.web.exception.ErrorCode.WRONG_ENTITY_INFORMATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Local big tests (or blackbox test / End to End)
 * Pushes an API call, checks that DB has expected datas
 * 
 **/
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:import-dev.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete-dev.sql")
})
public class TodoControllerBigTest extends WebAppTest {

    @Autowired
    private TodoRepository todoRepository;

    private int firstTodoId = 1;
    private String firstTodoDescription = "First Todo Unchecked";

    @Test
    public void should_Get_AllTodos_WithTwoTestTodoInResult() {
        given()
            .log().all()
        .when()
            .get("/todo")
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("[0].id", is(firstTodoId))
            .body("[0].description", is(firstTodoDescription));
    }

    @Test
    public void should_Get_OneTodoById_WithOneTestTodoInResult() {
        given()
            .log().all()
        .when()
            .get("/todo/{id}", firstTodoId)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(firstTodoId))
            .body("todo.description", is(firstTodoDescription));
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
        final String updatedDescription = "NewDescription of todo";

        TodoDTO todoDTO = new TodoDTO(newTodo().withDescription(updatedDescription).build());
        
        given()
            .header("Content-Type", "application/json")
            .body(todoDTO)
            .log().all()
        .when()
            .put("/todo/{id}", firstTodoId)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("todo.id", is(1))
            .body("todo.description", is(updatedDescription));

        Todo updatedTodo = todoRepository.findByDescription(updatedDescription);

        assertThat(updatedTodo, notNullValue());
        assertThat(updatedTodo.getId(), is(Long.valueOf(firstTodoId)));
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
            .delete("/todo/{id}", firstTodoId)
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
