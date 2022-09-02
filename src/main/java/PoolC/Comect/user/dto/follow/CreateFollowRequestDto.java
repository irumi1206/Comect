package PoolC.Comect.user.dto.follow;

import lombok.Data;

@Data
public class CreateFollowRequestDto {
    private String email;
    private String followedNickname;
}
