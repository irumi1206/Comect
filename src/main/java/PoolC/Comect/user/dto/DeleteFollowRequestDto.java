package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class DeleteFollowRequestDto {
    private String email;
    private String followedNickname;
}
