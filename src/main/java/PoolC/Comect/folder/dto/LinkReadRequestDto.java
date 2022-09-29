package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LinkReadRequestDto {

    private String path;
    private String id;

    @Builder
    public LinkReadRequestDto(String path,String id){
        this.path=path;
        this.id=id;
    }
}
