package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FolderReadPublicRequestDto {

    private String email;
    private String path;

    @Builder
    public FolderReadPublicRequestDto(String email, String path){
        this.email=email;
        this.path=path;
    }
}
