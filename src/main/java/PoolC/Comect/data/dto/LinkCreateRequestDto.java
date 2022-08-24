package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LinkCreateRequestDto {

    private String userEmail;
    private String path;
    private String linkTitle;
    private String linkImage;
    private String linkUrl;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkCreateRequestDto(String userEmail, String path, String linkTitle, String linkImage, String linkUrl, List<String> keywords, String isPublic){
        this.userEmail=userEmail;
        this.path=path;
        this.linkTitle=linkTitle;
        this.linkImage=linkImage;
        this.linkUrl=linkUrl;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }
}
