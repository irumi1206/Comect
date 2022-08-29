package PoolC.Comect.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowInfo {
    private String email;
    private String nickname;
    private String imageUrl;

    public FollowInfo(String email, String nickname, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}