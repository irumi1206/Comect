package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FolderReadPublicRequestDto {

    private String path;

    @Builder
    public FolderReadPublicRequestDto(String path){
        this.path=path;
    }
}
