package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String userEmail;
    private String userPassword;
}