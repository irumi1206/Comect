package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String profilePicture;
}
