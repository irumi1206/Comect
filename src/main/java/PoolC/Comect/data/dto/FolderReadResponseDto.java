package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Link;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FolderReadResponseDto {

    private List<String> folderNames;
    private List<Link> links;

    @Builder
    public FolderReadResponseDto(List<String> folderNames, List<Link> links){
        this.folderNames=folderNames;
        this.links=links;
    }
}
