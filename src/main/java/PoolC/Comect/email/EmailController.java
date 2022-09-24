package PoolC.Comect.email;

import PoolC.Comect.email.dto.EmailAuthLinkRequestDto;
import PoolC.Comect.email.dto.EmailAuthRequestDto;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.LoginRequestDto;
import PoolC.Comect.user.dto.LoginResponseDto;
import PoolC.Comect.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @ApiOperation(value="이메일 인증", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 전송."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 비밀번호 형식"),
            @ApiResponse(responseCode = "404", description = "이메일 전송 실패"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 닉네임")
    })
    @PostMapping("/authentication")
    public ResponseEntity<LoginResponseDto> emailAuth(@Valid @RequestBody EmailAuthRequestDto request){
        emailService.emailSend(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="이메일 인증 완료", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증됨"),
    })
    @GetMapping("/authenticationCheck")
    public ResponseEntity<LoginResponseDto> emailAuthSuccess(@Valid @ModelAttribute EmailAuthLinkRequestDto request){
        emailService.emailCheck(request.getId());

        return ResponseEntity.ok().build();
    }
}
