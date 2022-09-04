package PoolC.Comect.user.dto.follow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateFollowResponseDto {
    private String email;
    private String nickname;
    private String imageUrl;
    private boolean isFollowing;

    public CreateFollowResponseDto(String email, String nickname, String imageUrl, boolean isFollowing) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.isFollowing=isFollowing;
    }
}
