package PoolC.Comect.user.dto.follow;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class DeleteFollowEmailRequestDto {
    @Email
    private String followedEmail;
}
