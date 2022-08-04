package PoolC.Comect.domain.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "data")
public class Data {

    @Id
    private String id;

    private String folderName;
    private String picture;
    private List<Folder> folders;
    private List<Link> links;
}
