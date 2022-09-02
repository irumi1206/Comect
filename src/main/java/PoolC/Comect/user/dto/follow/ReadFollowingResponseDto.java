package PoolC.Comect.user.dto;

import PoolC.Comect.user.domain.FollowInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReadFollowingResponseDto {
    private int numberOfFollowing;
    private List<FollowInfo> followings;
}
