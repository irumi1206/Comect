package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class CreateFollowRequestDto {
    private String email;
    private String followedNickname;
}
