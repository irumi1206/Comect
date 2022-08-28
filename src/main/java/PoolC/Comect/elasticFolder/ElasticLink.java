package PoolC.Comect.elasticFolder;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="elasticLink")
public class ElasticLink {

    @Id
    private String id;

    private String nickName;
    private String path;
    private String linkName;

    public ElasticLink(String id, String nickName, String path, String linkName) {
        this.id = id;
        this.nickName = nickName;
        this.path = path;
        this.linkName = linkName;
    }
}
