package PoolC.Comect.user.dto.follow;

import lombok.Data;

@Data
public class CreateFollowNicknameRequestDto {
    private String email;
    private String followedNickname;
}
