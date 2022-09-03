package PoolC.Comect.elasticFolder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ElasticLinkSearchInfo {

    private String ownerId;
    private String nickName;
    private String path;
    private String linkId;
    private String linkName;
    private String imageUrl;
    private String url;
    private List<String> keywords;

    @Builder
    public ElasticLinkSearchInfo(String ownerId, String nickName, String path,String linkId, String linkName, String imageUrl, String url, List<String> keywords){
        this.ownerId=ownerId;
        this.nickName=nickName;
        this.path=path;
        this.linkId=linkId;
        this.linkName=linkName;
        this.imageUrl=imageUrl;
        this.url=url;
        this.keywords=keywords;
    }
}
