package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkCreateRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public LinkCreateRequestDto(String userEmail, String path){
        this.userEmail=userEmail;
        this.path=path;
    }
}
