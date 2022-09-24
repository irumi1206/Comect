package PoolC.Comect.email.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailAuthRequestDto {

    @NotNull
    @Email
    private String email;
}
