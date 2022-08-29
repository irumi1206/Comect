package PoolC.Comect.user.dto;

import PoolC.Comect.user.domain.FollowInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReadFollowResponseDto {
    private int numberOfFollowing;
    private int numberOfFollower;
    private List<FollowInfo> followings;
    private List<FollowInfo> followers;
}
