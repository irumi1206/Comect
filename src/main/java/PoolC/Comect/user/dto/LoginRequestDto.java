package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDto {

    @Email(message = "이메일을 입력하시오")
    private String email;
    @Size(min=6,max=15,message = "비밀번호 길이를 만족시키십시오")
    private String password;
}
