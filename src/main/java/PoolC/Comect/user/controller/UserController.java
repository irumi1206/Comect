package PoolC.Comect.user.controller;

import PoolC.Comect.relation.controller.RelationController;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.*;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/member")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequestDto request){
        log.info(
                "Called POST/member, \tbody: "+request.toString()
        );
        userService.join(request.getEmail(),request.getPassword(),request.getNickname(), request.getImageUrl());
        log.trace("signup success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        log.info(
                "Called POST/login, \tbody: "+request.toString()
        );
        userService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto(request.getEmail());
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<ReadUserResponseDto> readUser(@ModelAttribute ReadUserRequestDto request){
        log.info("Called GET/my, \tparameter: "+request.toString());
        User user = userService.findOne(request.getEmail());
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @PutMapping("/my")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserRequestDto request){
        log.info(
                "Called PUT/my, \tparameter: "+request.toString());
        userService.update(request.getEmail(), request.getNewNickname(), request.getNewImageUrl());
        return ResponseEntity.ok().build();
    }

}

