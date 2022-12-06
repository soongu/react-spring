package com.example.demo.todo.repository;

import com.example.demo.todo.entity.TodoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TodoRepository {

    List<TodoEntity> findByUserId(String userId);

    void save(TodoEntity entity);

    Optional<TodoEntity> findById(String id);

    void delete(TodoEntity entity);

    void modify(TodoEntity entity);
}
