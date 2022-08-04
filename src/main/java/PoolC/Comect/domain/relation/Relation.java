package PoolC.Comect.domain.relation;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="relation")
@Getter
@Setter
public class Relation {
    @Id
    @Generated
    private String id;
    private RelationType relationType;
    private String relationId1;
    private String relationId2;

    public Relation(String relationId1, String relationId2) {
        this.relationId1 = relationId1;
        this.relationId2 = relationId2;
        this.relationType=RelationType.REQUEST;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "id='" + id + '\'' +
                ", relationType=" + relationType +
                ", relationId1='" + relationId1 + '\'' +
                ", relationId2='" + relationId2 + '\'' +
                '}';
    }
}
