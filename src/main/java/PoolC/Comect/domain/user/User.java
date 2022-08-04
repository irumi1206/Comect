package PoolC.Comect.domain.user;

import PoolC.Comect.domain.relation.Relation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
@Getter
@Setter
public class User {

    @Id
    private String id;

    private String userNickname;
    private String email;
    private String rootFolderId;
    private String picture;
    private Relation relation;
}
