package PoolC.Comect.folder.domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;


@Getter
@Setter
public class Link {

    @Id
    private ObjectId _id;

    private String name;
    private ObjectId imageId;
    private String url;
    private List<String> keywords;
    private String isPublic;

    public Link(String name, ObjectId imageId, String url,List<String> keywords, String isPublic) {
        this._id=new ObjectId();
        this.name = name;
        this.imageId = imageId;
        this.url = url;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }

    @Override
    public String toString() {
        return "Link{" +
                "_id=" + _id +
                ", title='" + name + '\'' +
                ", image='" + imageId + '\'' +
                ", url='" + url + '\'' +
                ", keywords=" + keywords +
                ", isPublic='" + isPublic + '\'' +
                '}';
    }

    public String getImageUrl(){
        return "http://43.200.175.52:8080/image?id="+imageId.toHexString();
    }
}
