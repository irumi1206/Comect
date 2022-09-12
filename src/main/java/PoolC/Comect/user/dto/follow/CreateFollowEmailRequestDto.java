package PoolC.Comect.user.dto.follow;

import lombok.Data;

@Data
public class CreateFollowEmailRequestDto {
    private String email;
    private String followedEmail;
}
