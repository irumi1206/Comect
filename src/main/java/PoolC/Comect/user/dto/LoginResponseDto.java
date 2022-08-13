package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String userId;

    public LoginResponseDto(String userId) {
        this.userId = userId;
    }
}
