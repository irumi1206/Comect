package PoolC.Comect.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LinkReadRequestDto {

    private String userEmail;
    private String path;

    @Builder
    public LinkReadRequestDto(String userEmail,String path){
        this.userEmail=userEmail;
        this.path=path;
    }

}
