package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class FolderCheckPathRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderCheckPathRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }
}
