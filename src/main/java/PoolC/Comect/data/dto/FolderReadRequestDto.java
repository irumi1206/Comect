package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderReadRequestDto {
    private String userEmail;
    private String path;
    private String folderName;
    private String folderPicture;

    @Builder
    public FolderReadRequestDto(String path){
        this.path=path;
    }
}
