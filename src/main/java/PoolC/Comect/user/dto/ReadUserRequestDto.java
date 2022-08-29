package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ReadUserRequestDto {

    private String email;
}
