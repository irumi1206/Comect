package PoolC.Comect.elasticUser.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ElasticUserSearchRequestDto {

    private String email;
    private String keyword;

    @Builder
    public ElasticUserSearchRequestDto(String email, String keyword){
        this.email=email;
        this.keyword=keyword;
    }
}
