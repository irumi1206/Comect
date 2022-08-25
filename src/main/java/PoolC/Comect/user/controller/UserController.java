package PoolC.Comect.user.controller;

import PoolC.Comect.data.dto.FolderCreateRequestDto;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.*;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/member")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto request){
        userService.join(request.getEmail(),request.getPassword(),request.getNickname(), request.getImageUrl());
        log.trace("signup success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        userService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto(request.getEmail());
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<ReadUserResponseDto> readUser(@ModelAttribute ReadUserRequestDto request){
        User user = userService.findOne(request.getEmail());
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @PutMapping("/my")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto request){
        userService.update(request.getEmail(), request.getNewNickname(), request.getNewImageUrl());
        return ResponseEntity.ok().build();
    }

}

