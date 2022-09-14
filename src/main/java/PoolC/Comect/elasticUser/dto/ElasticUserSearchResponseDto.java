package PoolC.Comect.elasticUser.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ElasticUserSearchResponseDto {

    private List<String> nicknames;

    @Builder
    ElasticUserSearchResponseDto(List<String> nicknames){
        this.nicknames=nicknames;
    }
}
