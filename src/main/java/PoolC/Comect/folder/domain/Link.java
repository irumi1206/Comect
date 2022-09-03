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
    private String imageUrl;
    private String url;
    private List<String> keywords;
    private String isPublic;

    public Link(String name, String imageUrl, String url, List<String> keywords, String isPublic) {
        this._id = new ObjectId();
        this.name = name;
        this.imageUrl = imageUrl;
        this.url = url;
        this.keywords = keywords;
        this.isPublic = isPublic;
    }

    public Link(){

    }

    @Override
    public String toString() {
        return "Link{" +
                "_id=" + _id +
                ", title='" + name + '\'' +
                ", image='" + imageUrl + '\'' +
                ", url='" + url + '\'' +
                ", keywords=" + keywords +
                ", isPublic='" + isPublic + '\'' +
                '}';
    }

    public ObjectId getImageId(){
        if(this.imageUrl==null){
            return null;
        }
        String[] split = imageUrl.split("id=");
        if(split.length==1){
            return null;
        }
        String s = split[split.length - 1];
        return new ObjectId(s);
    }
}