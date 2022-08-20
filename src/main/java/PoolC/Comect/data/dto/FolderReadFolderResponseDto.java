package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FolderReadFolderResponseDto {

    private List<String> folderNames;

    @Builder
    public FolderReadFolderResponseDto(List<String> folderNames){
        this.folderNames=folderNames;
    }

}
