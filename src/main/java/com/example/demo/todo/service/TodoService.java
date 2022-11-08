package com.example.demo.todo.service;

import com.example.demo.todo.entity.TodoEntity;
import com.example.demo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoEntity> create(final TodoEntity entity) {
        //Validation
        validate(entity);
        todoRepository.save(entity);

        log.info("Entity Id : {} is saved.", entity.getId());

        return todoRepository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    private static void validate(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
