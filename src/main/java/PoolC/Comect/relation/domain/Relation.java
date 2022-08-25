package PoolC.Comect.relation.domain;

import PoolC.Comect.relation.domain.RelationType;
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
    private ObjectId senderId;
    private ObjectId receiverId;

    public Relation(ObjectId senderId, ObjectId receiverId) {
        this.id = new ObjectId();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.relationType=RelationType.REQUEST;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "id=" + id +
                ", relationType=" + relationType +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
