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
public class FolderCreateRequestDto {

    private String email;
    private String path;

    @NotNull
    @Size(max=10)
    @Pattern(regexp ="^[^/]+$")
    private String name;

    @Builder
    public FolderCreateRequestDto(String email, String path, String name){
        this.email=email;
        this.path=path;
        this.name=name;
    }
}
