package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkUpdateRequestDto {

    private String userEmail;
    private String path;
    private String linkName;
    private String linkPicture;
    private String link;

    @Builder
    public LinkUpdateRequestDto(String userEmail, String path, String linkName, String linkPicture,String link){
        this.userEmail=userEmail;
        this.path=path;
        this.linkName=linkName;
        this.linkPicture=linkPicture;
        this.link=link;
    }
}
