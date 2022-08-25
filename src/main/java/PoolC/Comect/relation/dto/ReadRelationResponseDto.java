package PoolC.Comect.relation.dto;

import PoolC.Comect.relation.domain.FriendInfo;
import lombok.Data;

import java.util.List;

@Data
public class ReadRelationResponseDto {
    private int numberOfRequest;
    private List<FriendInfo> request;
    private int numberOfFriend;
    private List<FriendInfo> friends;
}
