package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class FolderUpdateRequestDto {

    private String path;
    @NotNull
    @Size(max=10)
    @Pattern(regexp ="^[^/]+$")
    private String newName;

    @Builder
    public FolderUpdateRequestDto(String path, String newName){
        this.path=path;
        this.newName=newName;
    }
}
