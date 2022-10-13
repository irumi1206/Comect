package PoolC.Comect.elasticUser.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class ElasticUserInfo {

    private String email;
    private String nickname;
    private String imageUrl;
    private Boolean isFollowing;

    @Builder
    public ElasticUserInfo(String email,String nickname,String imageUrl,Boolean isFollowing){
        this.email=email;
        this.nickname=nickname;
        this.imageUrl=imageUrl;
        this.isFollowing=isFollowing;
    }

    public static ElasticUserInfo toElasticUserInfo(String email,String nickname,String imageUrl,Boolean isFollowing){

        return ElasticUserInfo.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .isFollowing(isFollowing)
                .build();
    }
}
