package com.example.demo.user.repository;

import com.example.demo.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);

    boolean save(UserEntity userEntity);

    String getProfile(String userId);
}
