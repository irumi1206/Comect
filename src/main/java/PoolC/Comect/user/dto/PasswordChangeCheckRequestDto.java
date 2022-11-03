package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class PasswordChangeCheckRequestDto {
    @Email
    private String email;
    private int randomNumber;
    @Size(min=6,max=15)
    private String newPassword;
}
