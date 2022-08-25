package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LinkCreateRequestDto {

    private String email;
    private String path;
    private String name;
    private String url;
    private String imageUrl;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkCreateRequestDto(String email, String path, String name, String imageUrl, String url, List<String> keywords, String isPublic){
        this.email=email;
        this.path=path;
        this.name= name;
        this.url=url;
        this.imageUrl=imageUrl;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }
}
