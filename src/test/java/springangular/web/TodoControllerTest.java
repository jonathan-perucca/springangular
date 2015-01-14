package springangular.web;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springangular.SpringangularApplication;
import springangular.domain.Todo;

import static com.jayway.restassured.RestAssured.config;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.LogConfig.logConfig;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringangularApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
public class TodoControllerTest {

    @Value("9000")
    int localPort;

    @Before
    public void setUp() {
        RestAssured.port = localPort;
        RestAssured.baseURI = "http://127.0.0.1";
    }

    @Test
    public void should_GetAllTodos_WithTwoTestTodoInResult() {
// with MockMvc implementation way
//        mockMvc.perform(get("/todo").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].title", is("Test")))
//                .andExpect(jsonPath("$[0].description", is("Description Test")))
//                .andDo(print());
        given()
            .log().all()
        .when()
            .get("/todo")
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("[0].id", is(1))
            .body("[0].title", is("Test"))
            .body("[0].description", is("Description Test"));

    }

    @Test
    public void should_GetOneTodoById_WithOneTestTodoInResult() throws Exception {
// with MockMvc implementation way
//        mockMvc.perform(get("/todo/1").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.title", is("Test")))
//                .andExpect(jsonPath("$.description", is("Description Test")))
//                .andDo(print());
        given()
            .log().all()
        .when()
            .get("/todo/{id}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("id", is(1))
            .body("title", is("Test"))
            .body("description", is("Description Test"));
    }

    @Test
    public void should_CreateOneTodo_Nominal() throws Exception {
//        mockMvc.perform(
//                put("/todo")
//                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                        .content(TestUtil.convertObjectToJsonBytes(todoToCreate)))
//                .andDo(print());
        Todo todoToCreate = new Todo.Builder().withTitle("NewTest").withDescription("NewDesc").build();
        given()
            .header("Content-Type", "application/json")
            .body(todoToCreate)
            .log().all()
        .when()
            .put("/todo")
        .then()
            .log().all()
            .statusCode(CREATED.value());
    }
    
}