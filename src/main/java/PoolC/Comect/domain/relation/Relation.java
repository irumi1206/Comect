package PoolC.Comect.domain.relation;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="relation")
@Getter
@Setter
public class Relation {
    @Id
    @Generated
    private ObjectId id;
    private RelationType relationType;
    private ObjectId relationId1;
    private ObjectId relationId2;

    public Relation(ObjectId relationId1, ObjectId relationId2) {
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
