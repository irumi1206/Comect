package PoolC.Comect.folder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FolderReadResponseDto {

    private List<FolderInfo> folderInfos;
    private List<LinkInfo> linkInfos;

    @Builder
    public FolderReadResponseDto(List<FolderInfo> folderInfos, List<LinkInfo> linkInfos){
        this.folderInfos=folderInfos;
        this.linkInfos=linkInfos;
    }
}
