package PoolC.Comect.user.controller;

import PoolC.Comect.user.domain.FollowInfo;
import PoolC.Comect.user.dto.follow.*;
import PoolC.Comect.user.dto.member.*;
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
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @ModelAttribute CreateUserRequestDto request){
        boolean success = userService.join(request.getEmail(), request.getPassword(), request.getNickname(), request.getMultipartFile());
        CreateUserResponseDto createUserResponseDto = CreateUserResponseDto.builder()
                .imageSuccess(success)
                .build();
        return ResponseEntity.ok(createUserResponseDto);
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
        User user = userService.findOneEmail(request.getEmail());
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @ApiOperation(value="내 정보 수정", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
            @ApiResponse(responseCode = "409", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @PutMapping("/member/me")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@Valid @ModelAttribute UpdateUserRequestDto request){
        boolean success = userService.update(request.getEmail(), request.getNewNickname(), request.getNewMultipartFile(), request.getImageChange());
        UpdateUserResponseDto updateUserResponseDto = UpdateUserResponseDto
                .builder()
                .imageSuccess(success)
                .build();
        return ResponseEntity.ok(updateUserResponseDto);
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

    @ApiOperation(value="팔로워 조회", notes="나를 팔로우 하는 사람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
    })
    @GetMapping("/follower")
    public ResponseEntity<ReadFollowerResponseDto> readFollower(@ModelAttribute ReadFollowRequestDto request){

        List<FollowInfo> followers = userService.readFollower(request.getEmail());
        ReadFollowerResponseDto readFollowResponseDto = ReadFollowerResponseDto.builder()
                .numberOfFollower(followers.size())
                .followers(followers)
                .build();
        return ResponseEntity.ok(readFollowResponseDto);
    }

    @ApiOperation(value="팔로잉 조회", notes="내가 팔로잉 하는 사람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
    })
    @GetMapping("/following")
    public ResponseEntity<ReadFollowingResponseDto> readFollowing(@ModelAttribute ReadFollowRequestDto request){

        List<FollowInfo> followings = userService.readFollowing(request.getEmail());
        ReadFollowingResponseDto readFollowingResponseDto = ReadFollowingResponseDto.builder()
                .numberOfFollowing(followings.size())
                .followings(followings)
                .build();
        return ResponseEntity.ok(readFollowingResponseDto);
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

    @ApiOperation(value="유저 탈퇴", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때, 비밀번호가 틀릴때"),
    })
    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteUserRequestDto request){
        userService.deleteMember(request.getEmail(),request.getPassword());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="팔로워, 팔로잉 최대 5명 조회", notes="")
    @ApiResponses({
    })
    @GetMapping("/follow/small")
    public ResponseEntity<ReadFollowSmallResponseDto> readFollow(@ModelAttribute ReadFollowSmallRequestDto request){

        List<FollowInfo> followers = userService.readFollowerSmall(request.getEmail());
        List<FollowInfo> followings = userService.readFollowingSmall(request.getEmail());
        ReadFollowSmallResponseDto readFollowSmallResponseDto = ReadFollowSmallResponseDto.builder()
                .numberOfFollowing(followings.size())
                .numberOfFollower(followers.size())
                .followings(followings)
                .followers(followers)
                .build();
        return ResponseEntity.ok(readFollowSmallResponseDto);
    }

    @ApiOperation(value="이메일 중복 체크", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
    @GetMapping("/email/valid")
    public ResponseEntity<Void> emailCheck(@ModelAttribute EmailValidRequestDto request){
        userService.validateDuplicateUser(request.getEmail());
        return ResponseEntity.ok().build();
    }
}

