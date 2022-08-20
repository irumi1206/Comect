package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String userEmail;

    public LoginResponseDto(String userEmail) {
        this.userEmail = userEmail;
    }
}
