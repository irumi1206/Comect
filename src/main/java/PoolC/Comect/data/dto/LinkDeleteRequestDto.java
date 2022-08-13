package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkDeleteRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public LinkDeleteRequestDto(String userEmail,String path){

    }
}
