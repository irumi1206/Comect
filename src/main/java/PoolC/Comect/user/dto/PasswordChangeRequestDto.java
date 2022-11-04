package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class PasswordChangeRequestDto {
    @Email
    private String email;
}
