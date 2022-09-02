package PoolC.Comect.user.dto.follow;

import PoolC.Comect.user.domain.FollowInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReadFollowSmallResponseDto {
    private int numberOfFollowing;
    private int numberOfFollower;
    private List<FollowInfo> followings;
    private List<FollowInfo> followers;
}
