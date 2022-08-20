package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderCreateRequestDto {

    private String userEmail;
    private String path;
    private String folderName;

    @Builder
    public FolderCreateRequestDto(String userEmail, String path, String folderName){
        this.userEmail=userEmail;
        this.path=path;
        this.folderName=folderName;
    }
}
