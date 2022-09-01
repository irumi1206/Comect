//package PoolC.Comect.elasticFolder.controller;
//
//import PoolC.Comect.elasticFolder.domain.ElasticFolder;
//import PoolC.Comect.elasticFolder.dto.ElasticFolderSearchInfo;
//import PoolC.Comect.elasticFolder.dto.ElasticFolderSearchRequestDto;
//import PoolC.Comect.elasticFolder.dto.ElasticFolderSearchResponseDto;
//
//import PoolC.Comect.elasticFolder.service.ElasticFolderService;
//import PoolC.Comect.user.repository.UserRepository;
//import PoolC.Comect.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.bson.types.ObjectId;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class ElasticFolderController {
//
//    private final ElasticFolderService elasticFolderService;
//    private final UserRepository userRepository;
//    private final UserService userService;
//
//    @GetMapping(value = "/folder/elastic")
//    public ResponseEntity<ElasticFolderSearchResponseDto> folderSearch(@ModelAttribute ElasticFolderSearchRequestDto elasticFolderSearchRequestDto) {
//
//        String email=elasticFolderSearchRequestDto.getEmail();
//        String keyword=elasticFolderSearchRequestDto.getKeyword();
//        String searchMe=elasticFolderSearchRequestDto.getSearchMe();
//
//        ObjectId ownerId=userService.findOneEmail(email).getId();
//
//        List<ElasticFolder> elasticFolderList=elasticFolderService.searchFolder(keyword);
//
//        List<ElasticFolderSearchInfo> myFolders=new ArrayList<>();
//        List<ElasticFolderSearchInfo> followingFolders=new ArrayList<>();
//        List<ElasticFolderSearchInfo> notFollowingFolders=new ArrayList<>();
//
//        System.out.println(elasticFolderSearchRequestDto);
//
//        for(ElasticFolder elasticFolder : elasticFolderList) {
//
//            String folderOwnerNickname=userRepository.findById(new ObjectId(elasticFolder.getOwnerId())).get().getNickname();
//
//            if (elasticFolder.getOwnerId().equals(ownerId) && searchMe.equals("true")) {
//                myFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
//            }
//            else if (userService.isFollower(new ObjectId(elasticFolder.getId()),ownerId) && searchMe.equals("false")) {
//                followingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
//            }
//            else if(searchMe.equals("false")) {
//                notFollowingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
//            }
//        }
//
//        ElasticFolderSearchResponseDto elasticFolderSearchResponseDto = ElasticFolderSearchResponseDto.builder()
//                .myFolders(myFolders)
//                .followingFolders(followingFolders)
//                .notFollowingFolders(notFollowingFolders)
//                .build();
//
//        return ResponseEntity.ok().body(elasticFolderSearchResponseDto);
//    }
//
//
//}
