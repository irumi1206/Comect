package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FolderReadFolderResponseDto {

    private List<FolderInfo> folderInfos;

    @Builder
    public FolderReadFolderResponseDto(List<FolderInfo> folderInfos){
        this.folderInfos=folderInfos;
    }

}
