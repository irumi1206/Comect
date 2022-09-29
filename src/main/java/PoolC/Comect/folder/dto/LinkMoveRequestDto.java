package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkMoveRequestDto {

    private String originalPath;
    private List<String> originalIds;
    private String modifiedPath;

    @Builder
    public LinkMoveRequestDto(String originalPath, List<String> originalIds, String modifiedPath){
        this.originalPath=originalPath;
        this.originalIds=originalIds;
        this.modifiedPath=modifiedPath;
    }

}
