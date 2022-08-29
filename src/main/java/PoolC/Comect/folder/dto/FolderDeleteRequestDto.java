package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FolderDeleteRequestDto {

    private String email;
    private List<String> paths;

    @Builder
    public FolderDeleteRequestDto(String email, List<String> paths){
        this.email=email;
        this.paths=paths;
    }

}
