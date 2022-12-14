package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class FolderMoveRequestDto {

    private List<String> originalPaths;
    private String modifiedPath;

    @Builder
    public FolderMoveRequestDto(List<String> originalPaths, String modifiedPath){
        this.originalPaths=originalPaths;
        this.modifiedPath=modifiedPath;
    }
}
