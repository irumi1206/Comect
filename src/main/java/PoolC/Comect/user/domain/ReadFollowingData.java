package PoolC.Comect.user.domain;

import lombok.Data;

import java.util.List;

@Data
public class ReadFollowingData {

    private int numberOfFollowings;
    private List<FollowInfo> followings;
}
