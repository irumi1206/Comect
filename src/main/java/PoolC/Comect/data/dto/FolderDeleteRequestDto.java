package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderDeleteRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public FolderDeleteRequestDto(String userEmail, String path){
        this.userEmail=userEmail;
        this.path=path;
    }

}
