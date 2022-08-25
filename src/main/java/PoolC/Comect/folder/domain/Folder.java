package PoolC.Comect.folder.domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="folder")
@Getter
@Setter
public class Folder {

    @Id
    ObjectId _id;
    private String name;
    private List<Folder> folders;
    private List<Link> links;

    public Folder(String name) {
        this._id = new ObjectId();
        this.name = name;
        folders=new ArrayList<>();
        links=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Folder{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
