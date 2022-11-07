package com.example.demo.todo.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private String error;
    private List<T> data;
}
