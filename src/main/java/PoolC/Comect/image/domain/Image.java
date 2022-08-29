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
    private String imageName;
    private String imagePath;

    public Image(String imageName) {
        this.id=new ObjectId();
        this.imageName = imageName;
    }
}
