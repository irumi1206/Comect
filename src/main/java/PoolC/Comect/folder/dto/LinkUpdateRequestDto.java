package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LinkUpdateRequestDto {

    private String id;
    private String email;
    private String path;
    private String name;
    private String url;
    private String imageUrl;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkUpdateRequestDto(String id, String email, String path, String name,String url,String imageUrl,List<String> keywords,String isPublic){
        this.id=id;
        this.email=email;
        this.path=path;
        this.name=name;
        this.url=url;
        this.imageUrl=imageUrl;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }
}
