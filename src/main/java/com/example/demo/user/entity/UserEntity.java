package com.example.demo.user.entity;

import lombok.*;

import java.util.UUID;

@Setter @Getter @ToString
@AllArgsConstructor
@Builder
public class UserEntity {

    private String id;
    private String username;
    private String email;
    private String password;

    public UserEntity() {
        this.id = UUID.randomUUID().toString();
    }
}
