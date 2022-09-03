package PoolC.Comect.user.dto.member;

import PoolC.Comect.user.domain.User;
import lombok.Data;

@Data
public class ReadUserResponseDto {

    private String email;
    private String nickname;
    private String imageUrl;

    public ReadUserResponseDto(User user){
        this.email=user.getEmail();
        this.nickname=user.getNickname();
        this.imageUrl=user.getImageUrl();
    }
}
