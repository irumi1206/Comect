package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderUpdateRequestDto {

    private String email;
    private String path;
    private String newName;

    @Builder
    public FolderUpdateRequestDto(String email, String path, String folderName, String newName){
        this.email=email;
        this.path=path;
        this.newName=newName;
    }
}
