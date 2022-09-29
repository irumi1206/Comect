package PoolC.Comect.elasticFolder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ElasticFolderSearchRequestDto {

    private String keyword;
    private String searchMe;

    @Builder
    public ElasticFolderSearchRequestDto(String keyword, String searchMe){
        this.keyword=keyword;
        this.searchMe=searchMe;
    }
}
