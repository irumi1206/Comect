package PoolC.Comect.user.dto;

import PoolC.Comect.user.domain.User;
import lombok.Data;

@Data
public class ReadUserResponseDto {

    private String nickname;
    private String imageUrl;

    public ReadUserResponseDto(User user){
        this.nickname=user.getNickname();
        this.imageUrl=user.getImageUrl();
    }
}
