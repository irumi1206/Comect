package PoolC.Comect.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowInfo {
    private String email;
    private String nickname;
    private String imageUrl;
    private Boolean isFollowing;

    public FollowInfo(String email, String nickname, String imageUrl, Boolean isFollowing) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.isFollowing=isFollowing;
    }

    public FollowInfo(){

    }
}