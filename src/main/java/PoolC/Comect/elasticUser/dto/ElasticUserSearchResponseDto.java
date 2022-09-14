package PoolC.Comect.elasticUser.dto;

import PoolC.Comect.elasticUser.domain.ElasticUserInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ElasticUserSearchResponseDto {

    private List<ElasticUserInfo> users;

    @Builder
    ElasticUserSearchResponseDto(List<ElasticUserInfo> users){
        this.users=users;
    }
}
