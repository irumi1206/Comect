package PoolC.Comect.user.dto;

import PoolC.Comect.user.domain.User;
import lombok.Data;

@Data
public class ReadUserResponseDto {

    private String nickname;
    private String profilePicture;

    public ReadUserResponseDto(User user){
        this.nickname=user.getUserNickname();
        this.profilePicture=user.getPicture();
    }
}
