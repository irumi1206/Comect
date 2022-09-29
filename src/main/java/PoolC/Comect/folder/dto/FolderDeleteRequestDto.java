package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class FolderDeleteRequestDto {

    private List<String> paths;

    @Builder
    public FolderDeleteRequestDto(List<String> paths){
        this.paths=paths;
    }

}
