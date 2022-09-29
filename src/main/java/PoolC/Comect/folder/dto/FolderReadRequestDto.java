package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FolderReadRequestDto {

    private String path;
    private String showLink;

    @Builder
    public FolderReadRequestDto(String path, String showLink){
        this.path=path;
        this.showLink=showLink;
    }
}
