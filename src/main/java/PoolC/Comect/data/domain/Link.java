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

    private String name;
    private String picture;
    private String url;
    private List<String> keywords;

    public Link(String name, String picture, String url) {
        this.name = name;
        this.picture = picture;
        this.url = url;
        this.keywords=new ArrayList<>();
    }

    public void addKeyword(String keyword){
        this.keywords.add(keyword);
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
