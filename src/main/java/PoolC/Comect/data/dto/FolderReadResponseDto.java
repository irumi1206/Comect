package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Link;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FolderReadResponseDto {

    private List<FolderInfo> folderInfos;
    private List<LinkInfo> linkInfos;

    @Builder
    public FolderReadResponseDto(List<FolderInfo> folderInfos, List<LinkInfo> linkInfos){
        this.folderInfos=folderInfos;
        this.linkInfos=linkInfos;
    }
}
