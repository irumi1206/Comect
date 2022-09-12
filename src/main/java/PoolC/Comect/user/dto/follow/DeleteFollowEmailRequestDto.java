package PoolC.Comect.user.dto.follow;

import lombok.Data;

@Data
public class DeleteFollowEmailRequestDto {
    private String email;
    private String followedEmail;
}
