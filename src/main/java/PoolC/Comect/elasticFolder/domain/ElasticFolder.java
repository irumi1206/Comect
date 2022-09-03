package PoolC.Comect.elasticFolder.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "elasticfolder", createIndex = true)
@Setting(settingPath = "noriAnalyzer.json")
@Data
public class ElasticFolder {

    @Id
    private String id;

    @Field(type=FieldType.Keyword)
    private String ownerId;

    @Field(type=FieldType.Keyword)
    private String path;

    @Field(type=FieldType.Text, analyzer="nori_analyzer")
    private String folderName;

    public ElasticFolder(String ownerId, String path, String folderName) {
        this.id = new ObjectId().toString();
        this.ownerId = ownerId;
        this.path = path;
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "ElasticFolder{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", path='" + path + '\'' +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
