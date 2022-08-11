package PoolC.Comect.user;

import PoolC.Comect.domain.user.User;
import PoolC.Comect.user.dto.CreateUserRequestDto;
import PoolC.Comect.user.dto.ReadUserRequestDto;
import PoolC.Comect.user.dto.ReadUserResponseDto;
import PoolC.Comect.user.dto.UpdateUserRequestDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller @ResponseBody
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signUp")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto request){
        userService.join(request.getUserNickname(), request.getUserEmail(), request.getProfilePicture(),request.getUserPassword());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/member/MyInfoChange")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto request){
        userService.update(request.getUserEmail(), request.getNewNickname(), request.getNewProfilePicture());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/member/MyInfo")
    public ReadUserResponseDto findUser(@RequestBody ReadUserRequestDto request){
        User user = userService.findOne(request.getUserEmail());
        return new ReadUserResponseDto(user);
    }

}
