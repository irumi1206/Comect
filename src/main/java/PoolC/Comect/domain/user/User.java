package PoolC.Comect.domain.user;

import PoolC.Comect.domain.relation.Relation;
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

    private String userNickname;
    private String email;
    private ObjectId rootFolderId;
    private String picture;
    private List<ObjectId> relations;
    private String password;

    public User(String userNickname, String email, ObjectId rootFolderId, String picture, String password) {
        this.userNickname = userNickname;
        this.email = email;
        this.rootFolderId = rootFolderId;
        this.picture = picture;
        this.relations=new ArrayList<>();
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", email='" + email + '\'' +
                ", rootFolderId='" + rootFolderId + '\'' +
                ", picture='" + picture + '\'' +
                ", relations=" + relations +
                '}';
    }
}
