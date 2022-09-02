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
public class LinkCreateRequestDto {

    private String email;
    private String path;
    @NotNull
    @Size(max=50)
    private String name;
    private String url;
    private MultipartFile multipartFile;
    private List<String> keywords;
    private String isPublic;
    private Boolean imageChange;

    @Builder
    public LinkCreateRequestDto(String email, String path, String name, MultipartFile multipartFile, String url, List<String> keywords, String isPublic,Boolean imageChange){
        this.email=email;
        this.path=path;
        this.name= name;
        this.url=url;
        this.multipartFile=multipartFile;
        this.keywords=keywords;
        this.isPublic=isPublic;
        this.imageChange = imageChange;
    }
}
