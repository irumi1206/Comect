package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FolderReadRequestDto {

    private String email;
    private String path;
    private String showLink;

    @Builder
    public FolderReadRequestDto(String email, String path, String showLink){
        this.email=email;
        this.path=path;
        this.showLink=showLink;
    }
}
