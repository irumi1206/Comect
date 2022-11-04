package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class EmailValidRequestDto {
    @Email
    private String email;
}
