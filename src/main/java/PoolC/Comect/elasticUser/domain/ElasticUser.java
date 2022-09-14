package PoolC.Comect.elasticUser.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "elasticuser", createIndex = true)
@Setting(settingPath = "ngramAnalyzer.json")
@Data
public class ElasticUser {

    @Id
    private String id;

    @Field(type=FieldType.Keyword)
    private String userId;

    @Field(type=FieldType.Text, analyzer="ngram_analyzer")
    private String nickname;

    public ElasticUser(String userId, String nickname){
        this.id=new ObjectId().toString();
        this.userId=userId;
        this.nickname=nickname;
    }
}
