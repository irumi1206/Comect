package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDto {

    @Email
    private String email;
    @Size(min=6,max=15)
    private String password;
}
