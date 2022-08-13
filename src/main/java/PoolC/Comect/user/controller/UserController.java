package PoolC.Comect.user.controller;

import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.*;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signUp")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto request){
        try{
            userService.join(request.getUserEmail(),request.getUserPassword(),request.getUserNickname(), request.getProfilePicture());
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        try{
            User user = userService.findOne(request.getUserEmail());
            LoginResponseDto loginResponseDto = new LoginResponseDto(request.getUserEmail());
            if(user.getPassword().equals(request.getUserPassword())){
                return ResponseEntity.ok(loginResponseDto);
            }else{
                return ResponseEntity.badRequest().build();
            }
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/member/myInfo")
    public ResponseEntity<ReadUserResponseDto> readUser(@RequestBody ReadUserRequestDto request){
        try{
            User user = userService.findOne(request.getUserEmail());
            return ResponseEntity.ok(new ReadUserResponseDto(user));
        }catch(IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/member/myInfoChange")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto request){
        try{
            userService.update(request.getUserEmail(), request.getNewNickname(), request.getNewProfilePicture());
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
