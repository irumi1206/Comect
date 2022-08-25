package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderCreateRequestDto {

    private String email;
    private String path;
    private String name;

    @Builder
    public FolderCreateRequestDto(String email, String path, String name){
        this.email=email;
        this.path=path;
        this.name=name;
    }
}
