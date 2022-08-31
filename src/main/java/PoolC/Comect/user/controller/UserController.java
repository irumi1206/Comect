package PoolC.Comect.user.controller;

import PoolC.Comect.user.domain.FollowInfo;
import PoolC.Comect.user.service.UserService;
import PoolC.Comect.user.domain.MemberData;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.*;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value="회원가입", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 비밀번호 형식 또는 닉네임"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 닉네임")
    })
    @PostMapping("/member")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequestDto request){

        userService.join(request.getEmail(),request.getPassword(),request.getNickname(), request.getImageUrl());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="로그인", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 비밀번호 형식"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않음, 로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){

        userService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto(request.getEmail());
        return ResponseEntity.ok(loginResponseDto);
    }

    @ApiOperation(value="내 정보 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/member/me")
    public ResponseEntity<ReadUserResponseDto> readUser(@ModelAttribute ReadUserRequestDto request){
        User user = userService.findOne(request.getEmail());
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @ApiOperation(value="내 정보 수정", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @PutMapping("/member/me")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserRequestDto request){

        userService.update(request.getEmail(), request.getNewNickname(), request.getNewImageUrl());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="팔로잉", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 팔로우")
    })
    @PostMapping("/follow")
    public ResponseEntity<Void> createFollow(@RequestBody CreateFollowRequestDto request){

        userService.createFollow(request.getEmail(),request.getFollowedNickname());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="팔로워 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
    })
    @GetMapping("/follow")
    public ResponseEntity<ReadFollowResponseDto> readFollow(@ModelAttribute ReadFollowRequestDto request){

        List<FollowInfo> followers = userService.readFollower(request.getEmail());
        List<FollowInfo> followings = userService.readFollowing(request.getEmail());
        ReadFollowResponseDto readFollowResponseDto = ReadFollowResponseDto.builder()
                .numberOfFollowing(followings.size())
                .numberOfFollower(followers.size())
                .followings(followings)
                .followers(followers)
                .build();
        return ResponseEntity.ok(readFollowResponseDto);
    }

    @ApiOperation(value="팔로워 취소", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
    })
    @DeleteMapping("/follow")
    public ResponseEntity<Void> deleteFollow(@RequestBody DeleteFollowRequestDto request){

        userService.deleteFollow(request.getEmail(),request.getFollowedNickname());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="유저 검색", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
    })
    @GetMapping("/member")
    public ResponseEntity<GetMemberResponseDto> getMember(@ModelAttribute GetMemberRequestDto request){

        MemberData memberInfo = userService.getMemberInfo(request.getNickname());
        GetMemberResponseDto getMemberResponseDto = GetMemberResponseDto.builder()
                .imageUrl(memberInfo.getImageUrl())
                .nickname(memberInfo.getNickname())
                .follower(memberInfo.getFollower())
                .following(memberInfo.getFollowing())
                .build();
        return ResponseEntity.ok(getMemberResponseDto);
    }
}

