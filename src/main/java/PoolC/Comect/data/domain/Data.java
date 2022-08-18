package PoolC.Comect.data.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "data")
@Getter
@Setter
public class Data {

    @Id
    private ObjectId id;
    private List<Folder> folders;
    private List<Link> links;

    public Data() {
        this.id = new ObjectId();
        this.folders=new ArrayList<>();
        this.links=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
