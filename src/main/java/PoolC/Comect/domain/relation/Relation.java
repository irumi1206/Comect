package PoolC.Comect.domain.relation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="relation")
@Getter
@Setter
public class Relation {
    @Id
    private String id;

    private String relationId1;
    private String relationId2;
}
