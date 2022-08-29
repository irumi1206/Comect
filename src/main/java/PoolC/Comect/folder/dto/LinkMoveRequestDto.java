package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class LinkMoveRequestDto {

    private String email;
    private List<String> originalPaths;
    private List<String> originalIds;
    private String modifiedPath;

    @Builder
    public LinkMoveRequestDto(String email, List<String> originalPaths, List<String> originalIds, String modifiedPath){
        this.email=email;
        this.originalPaths=originalPaths;
        this.originalIds=originalIds;
        this.modifiedPath=modifiedPath;
    }

}
