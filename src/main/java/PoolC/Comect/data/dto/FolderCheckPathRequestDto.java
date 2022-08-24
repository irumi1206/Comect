package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderCheckPathRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public FolderCheckPathRequestDto(String userEmail, String path){
        this.userEmail=userEmail;
        this.path=path;
    }
}
