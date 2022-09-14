package PoolC.Comect.elasticUser.controller;

import PoolC.Comect.elasticUser.dto.ElasticUserSearchRequestDto;
import PoolC.Comect.elasticUser.dto.ElasticUserSearchResponseDto;
import PoolC.Comect.elasticUser.service.ElasticUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ElasticUserController {

    private final ElasticUserService elasticUserService;

    @GetMapping(value="/user/elastic")
    public ResponseEntity<ElasticUserSearchResponseDto> searchUser(@ModelAttribute ElasticUserSearchRequestDto elasticUserSearchRequestDto){
        String email= elasticUserSearchRequestDto.getEmail();
        String keyword= elasticUserSearchRequestDto.getKeyword();
        List<String> nicknames= elasticUserService.searchUser(keyword);
        ElasticUserSearchResponseDto elasticUserSearchResponseDto=ElasticUserSearchResponseDto.builder()
                .nicknames(nicknames)
                .build();

        return ResponseEntity.ok().body(elasticUserSearchResponseDto);
    }
}
