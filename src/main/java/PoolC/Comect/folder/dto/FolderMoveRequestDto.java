package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FolderMoveRequestDto {

    private String email;
    private List<String> originalPaths;
    private String modifiedPath;

    @Builder
    public FolderMoveRequestDto(String email, List<String> originalPaths, String modifiedPath, String modifiedFolderName){
        this.email=email;
        this.originalPaths=originalPaths;
        this.modifiedPath=modifiedPath;
    }
}
