package com.jperucca.springangular.service;

import com.jperucca.springangular.repository.TodoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoCleanerServiceMockTest {

    @InjectMocks
    private TodoCleanerService todoCleanerService;
    
    @Mock
    private TodoRepository todoRepository;
    
    @Mock
    private List todoList;
    
    @Test
    public void should_RemoveCheckedTodos_Nominal() {
        when(todoRepository.findByChecked(anyBoolean())).thenReturn(todoList);
        
        todoCleanerService.removeCheckedTodos();
        
        verify(todoRepository).delete(todoList);
    }
}