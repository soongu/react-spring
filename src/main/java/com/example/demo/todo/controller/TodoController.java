package com.example.demo.todo.controller;

import com.example.demo.todo.dto.ResponseDTO;
import com.example.demo.todo.dto.TodoDTO;
import com.example.demo.todo.entity.TodoEntity;
import com.example.demo.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // 1. TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2. 생성당시에는 id가 없어야 하므로 null로 초기화한다.
            entity.setId(null);

            // 3. 임시 사용자 아이디를 설정한다.
            entity.setUserId(temporaryUserId);

            // 4. 서비스를 이용해 할일 리스트를 받아온다.
            List<TodoEntity> entities = todoService.create(entity);

            // 5. 엔터티 리스트를 dto리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(responseDTO);
    }


    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(temporaryUserId);

        List<TodoEntity> entities = todoService.update(entity);
        log.info("update return - {}", entities);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(responseDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id) {

        log.info("delete request - {}", id);

        try {

            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(new TodoDTO(id, null, false));
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.delete(entity);
            log.info("delete return - {}", entities);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());


            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
}










