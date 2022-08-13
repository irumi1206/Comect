package PoolC.Comect.data.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class Link {

    private String name;
    private String picture;
    private String link;

    public Link(String name, String picture, String link) {
        this.name = name;
        this.picture = picture;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Link{" +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
