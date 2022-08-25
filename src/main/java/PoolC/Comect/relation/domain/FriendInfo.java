package PoolC.Comect.relation.domain;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class FriendInfo {
    private String email;
    private String nickname;
    private String imageUrl;

    public FriendInfo(String email, String nickname, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
