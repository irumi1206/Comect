package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LinkDeleteRequestDto {

    private String email;
    private List<String> paths;
    private List<String> ids;

    @Builder
    public LinkDeleteRequestDto(String email,List<String> paths,List<String> ids){
        this.email=email;
        this.paths=paths;
        this.ids=ids;
    }
}
