package PoolC.Comect.data.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Link {

    @Id
    private ObjectId id;

    private String title;
    private String image;
    private String url;
    private List<String> keywords;
    private String isPublic;

    public Link(String title, String image, String url,List<String> keywords, String isPublic) {
        this.id=new ObjectId();
        this.title = title;
        this.image = image;
        this.url = url;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }

    public void addKeyword(String keyword){
        this.keywords.add(keyword);
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", keywords=" + keywords +
                ", isPublic='" + isPublic + '\'' +
                '}';
    }
}
