package PoolC.Comect.relation.dto;

import lombok.Data;

import java.io.FileReader;
import java.util.List;

@Data
public class ReadRelationResponseDto {
    private int numberOfRequest;
    private List<FriendInfo> request;
    private int numberOfFriends;
    private List<FriendInfo> friends;
}
