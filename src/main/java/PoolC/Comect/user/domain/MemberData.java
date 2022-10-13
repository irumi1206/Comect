package PoolC.Comect.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberData {
    private String nickname;
    private String imageUrl;
    private int follower;
    private int following;
    private Boolean isFollowing;
}
