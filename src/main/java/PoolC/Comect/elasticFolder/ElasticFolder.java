package PoolC.Comect.elasticFolder;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(indexName = "elasticfolder")
public class ElasticFolder {


    @Id
    private String id;

    private String nickName;
    private String path;
    private String folderName;

    public ElasticFolder(String id, String nickName, String path, String folderName) {
        this.id = id;
        this.nickName = nickName;
        this.path = path;
        this.folderName = folderName;
    }
}
