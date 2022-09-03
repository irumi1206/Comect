package PoolC.Comect.user.domain;

import PoolC.Comect.image.domain.Image;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="user")
@Getter
@Setter
public class User {

    @Id
    @Generated
    private ObjectId id;

    private String nickname;
    private String email;
    private ObjectId rootFolderId;
    private String imageUrl;
    private List<ObjectId> followings;
    private List<ObjectId> followers;
    private String password;

    public User(String nickname, String email, ObjectId rootFolderId, String imageUrl, String password) {
        this.id=new ObjectId();
        this.nickname = nickname;
        this.email = email;
        this.rootFolderId = rootFolderId;
        this.imageUrl = imageUrl;
        this.password = password;
        this.followings=new ArrayList<>();
        this.followers=new ArrayList<>();
    }
    public ObjectId getImageId(){
        if(this.imageUrl==null){
            return null;
        }
        String[] split = imageUrl.split("id=");
        String s = split[split.length - 1];
        return new ObjectId(s);
    }

}
