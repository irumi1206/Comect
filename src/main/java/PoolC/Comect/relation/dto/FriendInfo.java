package PoolC.Comect.relation.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class FriendInfo {
    private ObjectId id;
    private String nickname;
    private String profilePicture;
}
