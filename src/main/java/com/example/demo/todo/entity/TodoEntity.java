package com.example.demo.todo.entity;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoEntity {

    private String id;
    private String userId;
    private String title;
    private boolean done;
}
