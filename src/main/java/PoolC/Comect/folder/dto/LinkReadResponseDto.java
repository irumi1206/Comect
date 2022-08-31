package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LinkReadResponseDto {

    private String id;
    private String name;
    private String imageUrl;
    private String url;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkReadResponseDto(String id, String name, String imageUrl, String url, List<String> keywords,String isPublic){
        this.id=id;
        this.name=name;
        this.imageUrl=imageUrl;
        this.url=url;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }
}
