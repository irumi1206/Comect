package PoolC.Comect.user.dto.follow;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class CreateFollowEmailRequestDto {
    @Email
    private String followedEmail;
}
