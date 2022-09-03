package PoolC.Comect.elasticFolder.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "elasticlink", createIndex = true)
@Setting(settingPath = "noriAnalyzer.json")
@Data
public class ElasticLink {

    @Id
    private String id;

    @Field(type=FieldType.Keyword)
    private String ownerId;

    @Field(type=FieldType.Keyword)
    private String path;

    @Field(type=FieldType.Keyword)
    private String isPublic;

    @Field(type=FieldType.Keyword)
    private String linkId;

    @Field(type=FieldType.Text, analyzer="nori_analyzer")
    private String linkName;

    @Field(type=FieldType.Text, analyzer="standard")
    private String keywords;

    public ElasticLink(String ownerId, String path, String isPublic, String linkId, String linkName, String keywords) {
        this.id = new ObjectId().toString();
        this.ownerId = ownerId;
        this.path = path;
        this.isPublic=isPublic;
        this.linkId=linkId;
        this.linkName = linkName;
        this.keywords=keywords;
    }

}
