package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkUpdateRequestDto {

    private String id;
    private String path;
    @NotNull
    @Size(max=50)
    private String name;
    private String url;
    private MultipartFile multipartFile;
    private List<String> keywords;
    private String isPublic;
    private String imageChange;
    private String imageUrl;

    @Builder
    public LinkUpdateRequestDto(String id, String path, String name,String url,MultipartFile multipartFile,List<String> keywords,String isPublic,String imageChange,String imageUrl){
        this.id=id;
        this.path=path;
        this.name=name;
        this.url=url;
        this.multipartFile=multipartFile;
        this.keywords=keywords;
        this.isPublic=isPublic;
        this.imageChange=imageChange;
        this.imageUrl=imageUrl;
    }
}
