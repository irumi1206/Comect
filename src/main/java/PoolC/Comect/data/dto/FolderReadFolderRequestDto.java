package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderReadFolderRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public FolderReadFolderRequestDto(String userEmail, String path){
        this.userEmail=userEmail;
        this.path=path;
    }
}
