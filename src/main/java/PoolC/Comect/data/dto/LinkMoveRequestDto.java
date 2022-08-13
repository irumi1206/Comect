package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkMoveRequestDto {

    private String userEmail;
    private String originalPath;
    private String modifiedPath;

    @Builder
    public LinkMoveRequestDto(String userEmail, String originalPath, String modifiedPath){
        this.userEmail=userEmail;
        this.originalPath=originalPath;
        this.modifiedPath=modifiedPath;
    }

}
