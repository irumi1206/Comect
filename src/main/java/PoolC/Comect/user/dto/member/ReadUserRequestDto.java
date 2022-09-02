package PoolC.Comect.user.dto.member;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ReadUserRequestDto {
    @Email
    private String email;
}
