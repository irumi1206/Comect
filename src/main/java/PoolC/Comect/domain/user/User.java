package PoolC.Comect.domain.user;

import PoolC.Comect.domain.relation.Relation;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
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
    private String id;

    private String userNickname;
    private String email;
    private String rootFolderId;
    private String picture;
    private List<String> relations;

    public void addRelation(Relation relation){
        this.relations.add(relation.getId());
    }

    public User(String userNickname, String email, String rootFolderId, String picture) {
        this.userNickname = userNickname;
        this.email = email;
        this.rootFolderId = rootFolderId;
        this.picture = picture;
        this.relations=new ArrayList<>();
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
