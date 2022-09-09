package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String email;

    public LoginResponseDto(String email) {
        this.email = email;
    }
}
