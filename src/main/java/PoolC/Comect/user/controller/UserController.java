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

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/member")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto request){
        logger.info(
                "Called POST/member, \tbody: email="+request.getEmail()+
                ", \tpassword="+request.getPassword()+
                ", \tnickname="+request.getNickname()+
                ", \timageUrl="+request.getImageUrl()
        );
        userService.join(request.getEmail(),request.getPassword(),request.getNickname(), request.getImageUrl());
        log.trace("signup success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        logger.info(
                "Called POST/login, \tbody: email="+request.getEmail()+
                ", \tpassword="+request.getPassword()
        );
        userService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto(request.getEmail());
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<ReadUserResponseDto> readUser(@ModelAttribute ReadUserRequestDto request){
        logger.info("Called GET/my, \tparameter: email="+request.getEmail());
        User user = userService.findOne(request.getEmail());
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @PutMapping("/my")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto request){
        logger.info(
                "Called PUT/my, \tparameter: email="+request.getEmail()+
                ", \tnewNickname="+request.getNewNickname()+
                ", \tnewImageUrl="+request.getNewImageUrl());
        userService.update(request.getEmail(), request.getNewNickname(), request.getNewImageUrl());
        return ResponseEntity.ok().build();
    }

}

