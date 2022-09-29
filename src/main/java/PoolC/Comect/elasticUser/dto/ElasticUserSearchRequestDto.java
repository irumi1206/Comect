package PoolC.Comect.elasticUser.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ElasticUserSearchRequestDto {

    private String keyword;

    @Builder
    public ElasticUserSearchRequestDto(String keyword){
        this.keyword=keyword;
    }
}
