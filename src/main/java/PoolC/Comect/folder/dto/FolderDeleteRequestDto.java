package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderDeleteRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderDeleteRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }

}
