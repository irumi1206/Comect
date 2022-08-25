package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
}
