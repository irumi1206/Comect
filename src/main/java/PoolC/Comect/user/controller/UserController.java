package PoolC.Comect.user.controller;


import PoolC.Comect.common.infra.JwtTokenProvider;
import PoolC.Comect.user.domain.*;
//import PoolC.Comect.common.infra.JwtTokenProvider;
import PoolC.Comect.user.dto.follow.*;
import PoolC.Comect.user.dto.member.*;
import PoolC.Comect.user.service.UserService;
import PoolC.Comect.user.dto.*;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(path="/token/valid")
    public ResponseEntity<Void> getTokenValid(@ModelAttribute GetTokenValidRequestDto request, @AuthenticationPrincipal User user){
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="회원가입", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 비밀번호 형식 또는 닉네임"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 닉네임")
    })
    @PostMapping(path="/member")
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @ModelAttribute CreateUserRequestDto request){
        boolean success = userService.join(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname(), request.getMultipartFile());
        //boolean success = userService.join(request.getEmail(),request.getPassword(), request.getNickname(), request.getMultipartFile());

        CreateUserResponseDto createUserResponseDto = CreateUserResponseDto.builder()
                .imageSuccess(success)
                .build();
        return ResponseEntity.ok(createUserResponseDto);
    }

    @ApiOperation(value="로그인", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 비밀번호 형식"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않음, 로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request){

        User member=userService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
        //LoginResponseDto loginResponseDto = new LoginResponseDto(request.getEmail());

        System.out.println("토큰 발급 정보 : "+loginResponseDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @ApiOperation(value="내 정보 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/member/me")
    public ResponseEntity<ReadUserResponseDto> readUser(@ModelAttribute ReadUserRequestDto request, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(new ReadUserResponseDto(user));
    }

    @ApiOperation(value="내 정보 수정", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 수정됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임")
    })
    @PutMapping("/member/me")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@Valid @ModelAttribute UpdateUserRequestDto request,@AuthenticationPrincipal User user){
        boolean success = userService.update(user.getEmail(), request.getNewNickname(), request.getNewMultipartFile(), request.getImageChange());
        UpdateUserResponseDto updateUserResponseDto = UpdateUserResponseDto
                .builder()
                .imageSuccess(success)
                .build();
        return ResponseEntity.ok(updateUserResponseDto);
    }

    @ApiOperation(value="팔로우, 닉네임으로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 잘 됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 팔로우"),
            @ApiResponse(responseCode = "417", description = "나를 팔로우 시도")

    })
    @PostMapping("/follow/nickname")
    public ResponseEntity<CreateFollowResponseDto> createFollowByNickname(@RequestBody CreateFollowNicknameRequestDto request,@AuthenticationPrincipal User user){
        FollowInfo follow = userService.createFollowNickname(user.getEmail(), request.getFollowedNickname());
        CreateFollowResponseDto createFollowResponseDto = new CreateFollowResponseDto(follow.getEmail(), follow.getNickname(), follow.getImageUrl(), true);
//        CreateFollowResponseDto createFollowResponseDto = CreateFollowResponseDto.builder()
//                .isFollowing(true)
//                .email(follow.getEmail())
//                .imageUrl(follow.getImageUrl())
//                .nickname(follow.getNickname())
//                .build();
        return ResponseEntity.ok(createFollowResponseDto);
    }

    @ApiOperation(value="팔로우, 이메일로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 잘 됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 팔로우"),
            @ApiResponse(responseCode = "417", description = "나를 팔로우 시도")

    })
    @PostMapping("/follow/email")
    public ResponseEntity<CreateFollowResponseDto> createFollowByEmail(@RequestBody CreateFollowEmailRequestDto request,@AuthenticationPrincipal User user){
        FollowInfo follow = userService.createFollowEmail(user.getEmail(), request.getFollowedEmail());
        CreateFollowResponseDto createFollowResponseDto = new CreateFollowResponseDto(follow.getEmail(), follow.getNickname(), follow.getImageUrl(), true);
//        CreateFollowResponseDto createFollowResponseDto = CreateFollowResponseDto.builder()
//                .isFollowing(true)
//                .email(follow.getEmail())
//                .imageUrl(follow.getImageUrl())
//                .nickname(follow.getNickname())
//                .build();
        return ResponseEntity.ok(createFollowResponseDto);
    }



    @ApiOperation(value="팔로워 조회", notes="나를 팔로우 하는 사람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로워 조회됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/follower")
    public ResponseEntity<ReadFollowerResponseDto> readFollower(@ModelAttribute ReadFollowRequestDto request,@AuthenticationPrincipal User user){

        List<FollowInfo> followers = userService.readFollower(user.getEmail());
        ReadFollowerResponseDto readFollowResponseDto = ReadFollowerResponseDto.builder()
                .numberOfFollower(followers.size())
                .followers(followers)
                .build();
        return ResponseEntity.ok(readFollowResponseDto);
    }

    @ApiOperation(value="팔로잉 조회", notes="내가 팔로잉 하는 사람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로잉 조회됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/following")
    public ResponseEntity<ReadFollowingResponseDto> readFollowing(@ModelAttribute ReadFollowRequestDto request,@AuthenticationPrincipal User user){
        List<FollowInfo> followings = userService.readFollowing(user.getEmail());
        ReadFollowingResponseDto readFollowingResponseDto = ReadFollowingResponseDto.builder()
                .numberOfFollowing(followings.size())
                .followings(followings)
                .build();
        return ResponseEntity.ok(readFollowingResponseDto);
    }

    @ApiOperation(value="팔로워 취소, 닉네임으로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 취소됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때, 해당 팔로우가 없을때")
    })
    @DeleteMapping("/follow/nickname")
    public ResponseEntity<Void> deleteFollowByNickname(@RequestBody DeleteFollowNicknameRequestDto request,@AuthenticationPrincipal User user){
        userService.deleteFollowNickname(user.getEmail(),request.getFollowedNickname());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="팔로워 취소, 이메일로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 취소됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때, 해당 팔로우가 없을때")
    })
    @DeleteMapping("/follow/email")
    public ResponseEntity<Void> deleteFollowByEmail(@RequestBody DeleteFollowEmailRequestDto request,@AuthenticationPrincipal User user){
        userService.deleteFollowEmail(user.getEmail(),request.getFollowedEmail());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="유저 검색, 닉네임으로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 검색됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/member/nickname")
    public ResponseEntity<MemberData> getMember(@ModelAttribute GetMemberRequestDto request,@AuthenticationPrincipal User user){
        MemberData memberInfo = userService.getMemberInfo(user.getEmail(),request.getNickname());
        return ResponseEntity.ok(memberInfo);
    }

    @ApiOperation(value="유저 검색, 이메일로", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 검색됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/member/email")
    public ResponseEntity<MemberData> getMemberByEmail(@ModelAttribute GetMemberEmailRequestDto request,@AuthenticationPrincipal User user){

        MemberData memberInfo = userService.getMemberInfoByEmail(user.getEmail(),request.getOtherEmail());
        return ResponseEntity.ok(memberInfo);
    }

    @ApiOperation(value="유저 탈퇴", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 탈퇴됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때, 비밀번호가 틀릴때")
    })
    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteUserRequestDto request,@AuthenticationPrincipal User user){
        userService.deleteMember(user.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="팔로워, 팔로잉 최대 5명 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로워, 팔로잉 조회됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 유저가 없을때, 해당 이메일의 유저가 없을때")
    })
    @GetMapping("/follow/small")
    public ResponseEntity<ReadFollowSmallResponseDto> readFollow(@ModelAttribute ReadFollowSmallRequestDto request,@AuthenticationPrincipal User user){

        ReadFollowerData readFollowerData = userService.readFollowerSmall(user.getEmail());
        ReadFollowingData readFollowingData = userService.readFollowingSmall(user.getEmail());
        ReadFollowSmallResponseDto readFollowSmallResponseDto = ReadFollowSmallResponseDto.builder()
                .numberOfFollowing(readFollowingData.getNumberOfFollowings())
                .numberOfFollower(readFollowerData.getNumberOfFollowers())
                .followings(readFollowingData.getFollowings())
                .followers(readFollowerData.getFollowers())
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

    @PatchMapping("/password")
    public ResponseEntity<Void> passwordChangeEmail(@ModelAttribute PasswordChangeRequestDto request){
        userService.passwordChange(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/passwordCheck")
    public ResponseEntity<Void> passwordChangeCheck(@ModelAttribute PasswordChangeCheckRequestDto request){
        userService.passwordChangeCheck(request.getEmail(),request.getRandomNumber(),passwordEncoder.encode(request.getNewPassword()));
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/test")
//    public ResponseEntity<Void> testtest(){
//        userService.test();
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value="비밀번호 재설정 메일 전송", notes="")
//    @GetMapping("/password/email")
//    public ResponseEntity<Void> passwordResetMail(@ModelAttribute PasswordEmailRequestDto request){
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value="비밀번호 재설정 인증", notes="")
//    @GetMapping("/password/check")
//    public ResponseEntity<Void> passwordResetCheck(@ModelAttribute PasswordCheckRequestDto request){
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value="비밀번호 재설정", notes="")
//    @PostMapping("/password")
//    public ResponseEntity<Void> passwordReset(@ModelAttribute PasswordResetRequestDto request){
//        return ResponseEntity.ok().build();
//    }

}

