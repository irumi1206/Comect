package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String email;
    private String newNickname;
    private String newImageUrl;
}
