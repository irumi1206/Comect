//package PoolC.Comect.elasticFolder.domain;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.*;
//
//@Document(indexName = "elasticlink", createIndex = true)
//@Setting(settingPath = "noriAnalyzer.json")
//@Getter
//@Setter
//public class ElasticLink {
//
//    @Id
//    private String id;
//
//    @Field(type=FieldType.Keyword)
//    private String ownerId;
//
//    @Field(type=FieldType.Keyword)
//    private String path;
//
//    @Field(type=FieldType.Keyword)
//    private String isPublic;
//
//    @Field(type=FieldType.Keyword)
//    private String linkId;
//
//    @Field(type=FieldType.Text, analyzer="nori_analyzer")
//    private String linkName;
//
//    public ElasticLink(String ownerId, String path, String isPublic, String linkId, String linkName) {
//        this.id = new ObjectId().toString();
//        this.ownerId = ownerId;
//        this.path = path;
//        this.isPublic=isPublic;
//        this.linkId=linkId;
//        this.linkName = linkName;
//    }
//
//    @Override
//    public String toString() {
//        return "ElasticLink{" +
//                "id='" + id + '\'' +
//                ", ownerId='" + ownerId + '\'' +
//                ", path='" + path + '\'' +
//                ", isPublic='" + isPublic + '\'' +
//                ", linkName='" + linkName + '\'' +
//                '}';
//    }
//}
