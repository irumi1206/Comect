package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String userEmail;
    private String newNickname;
    private String newProfilePicture;
}
