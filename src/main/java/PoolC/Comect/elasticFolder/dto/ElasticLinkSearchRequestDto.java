package PoolC.Comect.elasticFolder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElasticLinkSearchRequestDto {

    private String keyword;
    private String searchMe;

    @Builder
    public ElasticLinkSearchRequestDto(String keyword, String searchMe){
        this.keyword=keyword;
        this.searchMe=searchMe;
    }
}
