package com.jperucca.springangular.web.dto;


import com.jperucca.springangular.config.DozerConfig;
import com.jperucca.springangular.domain.Todo;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jperucca.springangular.domain.Todo.newTodo;
import static com.jperucca.springangular.web.dto.TodoDTO.newTodoDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DozerConfig.class})
public class TodoDTOTest {

    @Autowired
    private Mapper mapper;

    @Test
    public void should_SerializeDTOToPojo_OnTodo() {
        long id = 1L;
        boolean checked = true;
        String description = "descr";
        Todo todo = newTodo().withId(id).withChecked(checked).withDescription(description).build();

        TodoDTO todoDTO = mapper.map(todo, TodoDTO.class);

        assertThat(todoDTO, notNullValue());
        assertThat(todoDTO.getId(), is(id));
        assertThat(todoDTO.isChecked(), is(checked));
        assertThat(todoDTO.getDescription(), is(description));
    }

    @Test
    public void should_SerializePojoToDTO_OnTodo() {
        long id = 1L;
        boolean checked = true;
        String description = "descr";
        TodoDTO todoDTO = newTodoDTO().withId(id).withChecked(checked).withDescription(description).build();

        Todo todo = mapper.map(todoDTO, Todo.class);

        assertThat(todo, notNullValue());
        assertThat(todo.getId(), is(id));
        assertThat(todo.isChecked(), is(checked));
        assertThat(todo.getDescription(), is(description));
    }
}