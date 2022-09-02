package PoolC.Comect.user.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMemberResponseDto {
    private String nickname;
    private String imageUrl;
    private int follower;
    private int following;
}
