package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderUpdateRequestDto {

    private String userEmail;
    private String path;
    private String folderName;
    private String folderPicture;

    @Builder
    public FolderUpdateRequestDto(String userEmail, String path, String folderName, String folderPicture){
        this.userEmail=userEmail;
        this.path=path;
        this.folderName=folderName;
        this.folderPicture=folderPicture;
    }
}
