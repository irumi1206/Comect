package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderReadRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderReadRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }
}
