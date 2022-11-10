package PoolC.Comect.elasticUser.service;

import PoolC.Comect.elasticUser.domain.ElasticUserInfo;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.elasticUser.domain.ElasticUser;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticUserService {

    private final ElasticUserRepository elasticUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<ElasticUserInfo> searchUser(String keyword, String email){

        User searchUser=userService.findOneEmail(email);

        List<String> nicknames=elasticUserRepository.findByNickname(keyword).stream().map(ElasticUser::getNickname).collect(Collectors.toList());
        List<ElasticUserInfo> elasticUserInfos=new ArrayList<>();

        for(int i=0;i<nicknames.size();++i){
            String nickname=nicknames.get(i);
            if(searchUser.getNickname().equals(nickname)) continue;
            Optional<User> userOption=userRepository.findByNickname(nickname);
            userOption.ifPresent(
                    currentUser->{
                        String imageUrl=currentUser.getImageUrl();
                        Boolean isFollowing=userService.isFollower(currentUser.getId(),searchUser.getId())?true:false;
                        String currentEmail=currentUser.getEmail();
                        elasticUserInfos.add(ElasticUserInfo.toElasticUserInfo(currentEmail,nickname,imageUrl,isFollowing));
                    }
            );
        }

        return elasticUserInfos;

    }

}
