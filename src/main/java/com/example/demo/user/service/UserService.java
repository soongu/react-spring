package com.example.demo.user.service;

import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists - {}", email);
            throw new RuntimeException("이메일이 중복되었습니다.");
        }

        boolean flag = userRepository.save(userEntity);

        if (flag) return userRepository.findByEmail(email);
        return null;
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        if (originalUser == null) throw new RuntimeException("가입된 회원이 아닙니다.");
        // 패스워드 일치 검사
        if (encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        } else {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

    }

    // 이메일 중복확인
    public boolean isDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public String findProfile(String userId) {
        String profile = userRepository.getProfile(userId);
        log.info("찾은 프로필: {}", profile);
        return profile;
    }
}








