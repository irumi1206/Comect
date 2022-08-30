package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class FolderUpdateRequestDto {

    private String email;
    private String path;
    @NotNull
    @Size(max=10)
    @Pattern(regexp ="^[^/]+$")
    private String newName;

    @Builder
    public FolderUpdateRequestDto(String email, String path, String newName){
        this.email=email;
        this.path=path;
        this.newName=newName;
    }
}
