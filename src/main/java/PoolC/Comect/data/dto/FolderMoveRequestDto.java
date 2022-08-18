package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderMoveRequestDto {

    private String userEmail;
    private String originalPath;
    private String modifiedPath;
    private String modifiedFolderName;

    @Builder
    public FolderMoveRequestDto(String userEmail, String originalPath, String modifiedPath, String modifiedFolderName){
        this.userEmail=userEmail;
        this.originalPath=originalPath;
        this.modifiedPath=modifiedPath;
        this.modifiedFolderName=modifiedFolderName;
    }
}
