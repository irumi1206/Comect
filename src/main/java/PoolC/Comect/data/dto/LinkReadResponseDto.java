package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Link;
import lombok.Builder;
import lombok.Data;

@Data
public class LinkReadResponseDto {

    private Link link;

    @Builder
    public LinkReadResponseDto(Link link){
        this.link=link;
    }
}
