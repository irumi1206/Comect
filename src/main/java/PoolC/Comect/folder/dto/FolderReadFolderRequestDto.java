package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderReadFolderRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderReadFolderRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }
}
