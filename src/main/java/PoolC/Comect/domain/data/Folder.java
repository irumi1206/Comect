package PoolC.Comect.domain.data;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
public class Folder {

    private String name;
    private String picture;
    private List<Folder> folders;
    private List<Link> links;

    public Folder(String name, String picture) {
        this.name = name;
        this.picture = picture;
        folders=new ArrayList<>();
        links=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Folder{" +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
