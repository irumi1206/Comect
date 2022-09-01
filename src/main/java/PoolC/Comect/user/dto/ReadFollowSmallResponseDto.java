package PoolC.Comect.user.dto;

import PoolC.Comect.user.domain.FollowInfo;
import lombok.Builder;

import java.util.List;


@Builder
public class ReadFollowSmallResponseDto {
    private int numberOfFollowing;
    private int numberOfFollower;
    private List<FollowInfo> followings;
    private List<FollowInfo> followers;
}
