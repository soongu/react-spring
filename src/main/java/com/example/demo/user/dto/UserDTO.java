package com.example.demo.user.dto;

import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String token;
    private String id;
    private String email;
    private String username;
    private String password;

    private String profileImg;
}
