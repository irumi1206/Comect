package PoolC.Comect.elasticUser.controller;

import PoolC.Comect.elasticUser.domain.ElasticUserInfo;
import PoolC.Comect.elasticUser.dto.ElasticUserSearchRequestDto;
import PoolC.Comect.elasticUser.dto.ElasticUserSearchResponseDto;
import PoolC.Comect.elasticUser.service.ElasticUserService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ElasticUserController {

    private final ElasticUserService elasticUserService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping(value="/user/elastic")
    public ResponseEntity<ElasticUserSearchResponseDto> searchUser(@ModelAttribute ElasticUserSearchRequestDto elasticUserSearchRequestDto){
        String email= elasticUserSearchRequestDto.getEmail();
        User searchUser=userService.findOneEmail(email);
        String keyword= elasticUserSearchRequestDto.getKeyword();
        List<String> nicknames= elasticUserService.searchUser(keyword);
        List<ElasticUserInfo> users=new ArrayList<>();

        for(int i=0;i<nicknames.size();++i){

            String nickname=nicknames.get(i);
            if(searchUser.getNickname().equals(nickname)) continue;
            Optional<User> user=userRepository.findByNickname(nickname);
            user.ifPresent(
                    currentUser->{
                        String imageUrl=currentUser.getImageUrl();
                        String isFollowing=userService.isFollower(currentUser.getId(),searchUser.getId())?"true":"false";
                        String currentEmail=currentUser.getEmail();
                        users.add(ElasticUserInfo.toElasticUserInfo(currentEmail,nickname,imageUrl,isFollowing));
                    }
            );
        }

        ElasticUserSearchResponseDto elasticUserSearchResponseDto=ElasticUserSearchResponseDto.builder()
                .users(users)
                .build();

        return ResponseEntity.ok().body(elasticUserSearchResponseDto);
    }
}
