package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class PasswordChangeCheckRequestDto {
    private String email;
    private int randomNumber;
    private String newPassword;
}
