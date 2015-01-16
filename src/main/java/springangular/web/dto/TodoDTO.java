package springangular.web.dto;

import springangular.domain.Todo;

public class TodoDTO {

    private Todo todo;

    public TodoDTO() {}

    public TodoDTO(Todo todo) {
        this.todo = todo;
    }
    
    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
