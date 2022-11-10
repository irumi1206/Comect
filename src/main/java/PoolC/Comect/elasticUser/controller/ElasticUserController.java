package PoolC.Comect.elasticUser.controller;

import PoolC.Comect.elasticUser.domain.ElasticUser;
import PoolC.Comect.elasticUser.domain.ElasticUserInfo;
import PoolC.Comect.elasticUser.dto.ElasticUserSearchRequestDto;
import PoolC.Comect.elasticUser.dto.ElasticUserSearchResponseDto;
import PoolC.Comect.elasticUser.service.ElasticUserService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;

    @GetMapping(value="/user/elastic")
    public ResponseEntity<ElasticUserSearchResponseDto> searchUser(@ModelAttribute ElasticUserSearchRequestDto elasticUserSearchRequestDto, @AuthenticationPrincipal User user){
        String email= user.getEmail();

        User searchUser=userService.findOneEmail(email);
        String keyword= elasticUserSearchRequestDto.getKeyword();
        List<ElasticUserInfo> users= elasticUserService.searchUser(keyword,email);

        ElasticUserSearchResponseDto elasticUserSearchResponseDto=ElasticUserSearchResponseDto.builder()
                .users(users)
                .build();

        return ResponseEntity.ok().body(elasticUserSearchResponseDto);
    }
}
