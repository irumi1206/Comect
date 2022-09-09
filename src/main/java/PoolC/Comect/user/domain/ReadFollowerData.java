package PoolC.Comect.user.domain;

import lombok.Data;

import java.util.List;

@Data
public class ReadFollowerData {
    private int numberOfFollowers;
    private List<FollowInfo> followers;
}
