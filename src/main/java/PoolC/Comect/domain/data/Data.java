package PoolC.Comect.domain.data;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "data")
@Getter
@Setter
public class Data {

    @Id
    private ObjectId id;
    private String folderName;
    private String picture;
    private List<Folder> folders;
    private List<Link> links;

    public Data(String folderName, String picture) {
        this.folderName = folderName;
        this.picture = picture;
        this.folders=new ArrayList<>();
        this.links=new ArrayList<>();
    }

    public void addFolder(Folder folder){
        this.folders.add(folder);
    }
    public void addLink(Link link){
        this.links.add(link);
    }

    @Override
    public String toString() {
        return "Data{" +
                //"id='" + id + '\'' +
                ", folderName='" + folderName + '\'' +
                ", picture='" + picture + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
