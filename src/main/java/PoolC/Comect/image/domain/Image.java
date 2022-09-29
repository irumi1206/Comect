package PoolC.Comect.image.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="image")
@Getter
@Setter
public class Image {
    @Id
    @Generated
    private ObjectId id;
    private String email;
    private String extender;
    private String ownerId;

    public Image(String extender,String email,String ownerId) {
        this.id=new ObjectId();
        this.extender=extender;
        this.email=email;
        this.ownerId=ownerId;
    }

    @Override
    public String toString() {
        return "http://43.200.175.52:8080/image?id="+id;
    }
}
