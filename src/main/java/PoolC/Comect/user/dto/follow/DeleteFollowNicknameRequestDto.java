package PoolC.Comect.user.dto.follow;

import lombok.Data;

@Data
public class DeleteFollowNicknameRequestDto {
    private String email;
    private String followedNickname;
}
