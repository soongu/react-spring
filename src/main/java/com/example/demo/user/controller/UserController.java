package com.example.demo.user.controller;

import com.example.demo.security.TokenProvider;
import com.example.demo.todo.dto.ResponseDTO;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> register(
            @RequestPart("user") UserDTO userDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImg) {

        try {
            UserEntity user = UserEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(encoder.encode(userDTO.getPassword()))
                    .build();

            log.info("/auth/signup - {}, file - {}", user, profileImg);

            if (profileImg != null) {
                String originalFilename = profileImg.getOriginalFilename();
                log.info("profileImg - {}", originalFilename);

                String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
                File uploadFile = new File(uploadPath + "/" + uniqueFileName);
                profileImg.transferTo(uploadFile);

                user.setProfileImg(uniqueFileName);
            }

            UserEntity registeredUser = userService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .profileImg(registeredUser.getProfileImg())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);


        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), encoder);

            // ?????? ??????
            final String token = tokenProvider.create(user);

            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token) // ?????? ??????
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);

        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @GetMapping("/check")
    public ResponseEntity<?> checkEmail(String email) {
        boolean flag = userService.isDuplicate(email);
        log.info("{} ?????????????? - {}", email, flag);
        return ResponseEntity.ok().body(flag);
    }

    @GetMapping("/load-profile")
    public ResponseEntity<byte[]> loadFile(@AuthenticationPrincipal String userId) throws IOException {

        log.info("/loadFile GET - {}", userId);

        // ?????????????????? ???????????? ????????? ?????? ????????? ???????????? ???????????? ???.

        //  ?????? ????????? ????????? file????????? ??????
        String fileName = userService.findProfile(userId);
        File f = new File(uploadPath + "/" + fileName);

        if (!f.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

        MediaType mediaType = MediaUtils.getMediaType(formatName);

        // ??????????????? ????????? ?????? ??????
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        //  ?????? ??????????????? ?????????????????? ??????.
        byte[] rawData = FileCopyUtils.copyToByteArray(f);

        return new ResponseEntity<>(rawData, headers, HttpStatus.OK); // ?????????????????? ?????? ????????? ??????


    }

}
