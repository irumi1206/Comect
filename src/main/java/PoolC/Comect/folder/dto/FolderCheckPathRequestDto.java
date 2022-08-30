package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor

public class FolderCheckPathRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderCheckPathRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }
}
