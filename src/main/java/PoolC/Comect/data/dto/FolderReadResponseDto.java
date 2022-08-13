package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.domain.Link;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FolderReadResponseDto {
    private List<Folder> folders;
    private List<Link> links;

    @Builder
    public FolderReadResponseDto(List<Folder> folders, List<Link> links){
        this.folders=folders;
        this.links=links;
    }

}
