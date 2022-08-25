package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkDeleteRequestDto {

    private String email;
    private String path;
    private String id;

    @Builder
    public LinkDeleteRequestDto(String email,String path,String id){
        this.email=email;
        this.path=path;
        this.id=id;
    }
}
