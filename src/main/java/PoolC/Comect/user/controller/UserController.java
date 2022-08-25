//package PoolC.Comect.user.controller;
//
//import PoolC.Comect.user.domain.User;
//import PoolC.Comect.user.dto.*;
//import PoolC.Comect.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/auth/signUp")
//    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto request) throws InterruptedException {
//        userService.join(request.getUserEmail(),request.getUserPassword(),request.getUserNickname(), request.getProfilePicture());
//        log.trace("signup success");
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/auth/login")
//    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) throws InterruptedException {
//        User user = userService.findOne(request.getUserEmail());
//        LoginResponseDto loginResponseDto = new LoginResponseDto(request.getUserEmail());
//        if(user.getPassword().equals(request.getUserPassword())){
//            return ResponseEntity.ok(loginResponseDto);
//        }else{
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @PostMapping("/member/myInfo")
//    public ResponseEntity<ReadUserResponseDto> readUser(@RequestBody ReadUserRequestDto request) throws InterruptedException {
//        User user = userService.findOne(request.getUserEmail());
//        return ResponseEntity.ok(new ReadUserResponseDto(user));
//    }
//
//    @PostMapping("/member/myInfoChange")
//    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto request){
//        userService.update(request.getUserEmail(), request.getNewNickname(), request.getNewProfilePicture());
//        return ResponseEntity.ok().build();
//    }
//
//}
