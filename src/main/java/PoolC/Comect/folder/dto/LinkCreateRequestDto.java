package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkCreateRequestDto {

    private String email;
    private String path;
    @NotNull
    @Size(max=50)
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
