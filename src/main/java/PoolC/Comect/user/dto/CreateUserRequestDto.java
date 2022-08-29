package PoolC.Comect.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestDto {

    @NotNull
    @Email
    private String email;

    @Size(min=6,max=15)
    private String password;

    @NotNull
    @Size(max=10)
    @Pattern(regexp="^[a-zA-z가-힣 ]+$")
    private String nickname;

    private String imageUrl;
}
