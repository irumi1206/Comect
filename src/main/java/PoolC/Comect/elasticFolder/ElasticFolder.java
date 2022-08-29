package PoolC.Comect.elasticFolder;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "elasticfolder", createIndex = true)
@Setting(settingPath = "noriAnalyzer.json")
public class ElasticFolder {

    @Id
    private String id;

    @Field(type=FieldType.Keyword)
    private String nickName;

    @Field(type=FieldType.Keyword)
    private String path;

    @Field(type=FieldType.Text, analyzer="nori_analyzer")
    private String folderName;

    public ElasticFolder(String id, String nickName, String path, String folderName) {
        this.id = id;
        this.nickName = nickName;
        this.path = path;
        this.folderName = folderName;
    }
}
