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

    @Id
    private ObjectId id;
    private String name;
    private String picture;
    private String parentFolderId;
    private List<Folder> folders;
    private List<Link> links;

    public Folder(String name, String picture) {
        this.id=new ObjectId();
        this.name = name;
        this.picture = picture;
        folders=new ArrayList<>();
        links=new ArrayList<>();
    }

    public void addFolder(Folder folder){
        this.folders.add(folder);
    }
    public void addLink(Link link){
        this.links.add(link);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", parentFolderName='" + parentFolderId + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
