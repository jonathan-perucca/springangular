package com.jperucca.springangular.service;


import com.jperucca.springangular.domain.Todo;
import com.jperucca.springangular.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Clean every 5 minutes, all checked todos (task marked as done)
 */
@Component
public class TodoCleanerService {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoCleanerService.class);

    @Autowired
    private TodoRepository todoRepository;

    // Every 5 Minutes
    @Scheduled(fixedDelayString = "${scheduler.todos.remove.checked}")
    public void removeCheckedTodos() {
        logger.info("Scheduler launched at {}", new Date());
        
        List<Todo> todoList = todoRepository.findByChecked(true);

        logger.info("Will delete : {}", todoList);
        
        todoRepository.delete(todoList);
    }
}
