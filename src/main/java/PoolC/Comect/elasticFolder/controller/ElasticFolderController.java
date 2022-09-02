package PoolC.Comect.elasticFolder.controller;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;
import PoolC.Comect.elasticFolder.dto.*;

import PoolC.Comect.elasticFolder.service.ElasticService;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ElasticFolderController {

    private final ElasticService elasticService;
    private final UserRepository userRepository;
    private final FolderService folderService;

    @GetMapping(value = "/folder/elastic")
    public ResponseEntity<ElasticFolderSearchResponseDto> folderSearch(@ModelAttribute ElasticFolderSearchRequestDto elasticFolderSearchRequestDto) {

        String email=elasticFolderSearchRequestDto.getEmail();
        String keyword=elasticFolderSearchRequestDto.getKeyword();
        String searchMe=elasticFolderSearchRequestDto.getSearchMe();

        Optional<User> user=userRepository.findByEmail(email);
        ObjectId ownerId=user.get().getId();

        List<ElasticFolderSearchInfo> myFolders=new ArrayList<>();
        List<ElasticFolderSearchInfo> followingFolders=new ArrayList<>();
        List<ElasticFolderSearchInfo> notFollowingFolders=new ArrayList<>();

        if(searchMe.equals("true")){
            List<String> searchIds=new ArrayList<>();
            searchIds.add(ownerId.toString());
            List<ElasticFolder> searchMeList= elasticService.searchFolder(searchIds,keyword);
            for(ElasticFolder elasticFolder:searchMeList){
                ObjectId folderOwnerId=new ObjectId(elasticFolder.getOwnerId());
                String folderOwnerNickname=userRepository.findById(folderOwnerId).get().getNickname();
                myFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
            }
        }
        else{
            List<String> followingIds=userRepository.findById(ownerId).get().getFollowers().stream().map(current->current.toString()).collect(Collectors.toList());
            List<ElasticFolder> searchFollowingList= elasticService.searchFolder(followingIds,keyword);
            followingIds.add(ownerId.toString());
            List<ElasticFolder> searchNotFollowingList= elasticService.searchExcludeFolder(followingIds,keyword);
            for(ElasticFolder elasticFolder:searchFollowingList){
                ObjectId folderOwnerId=new ObjectId(elasticFolder.getOwnerId());
                String folderOwnerNickname=userRepository.findById(folderOwnerId).get().getNickname();
                followingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
            }
            for(ElasticFolder elasticFolder:searchNotFollowingList){
                ObjectId folderOwnerId=new ObjectId(elasticFolder.getOwnerId());
                String folderOwnerNickname=userRepository.findById(folderOwnerId).get().getNickname();
                notFollowingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname));
            }
        }

        ElasticFolderSearchResponseDto elasticFolderSearchResponseDto = ElasticFolderSearchResponseDto.builder()
                .myFolders(myFolders)
                .followingFolders(followingFolders)
                .notFollowingFolders(notFollowingFolders)
                .build();

        return ResponseEntity.ok().body(elasticFolderSearchResponseDto);
    }

    @GetMapping(value = "/link/elastic")
    public ResponseEntity<ElasticLinkSearchResponseDto> linkSearch(@ModelAttribute ElasticLinkSearchRequestDto elasticLinkSearchRequestDto) {

        String email=elasticLinkSearchRequestDto.getEmail();
        String keyword=elasticLinkSearchRequestDto.getKeyword();
        String searchMe=elasticLinkSearchRequestDto.getSearchMe();

        Optional<User> user=userRepository.findByEmail(email);
        ObjectId ownerId=user.get().getId();

        List<ElasticLinkSearchInfo> myLinks=new ArrayList<>();
        List<ElasticLinkSearchInfo> followingLinks=new ArrayList<>();
        List<ElasticLinkSearchInfo> notFollowingLinks=new ArrayList<>();

        if(searchMe.equals("true")){
            List<String> searchIds=new ArrayList<>();
            searchIds.add(ownerId.toString());
            List<ElasticLink> searchMeList= elasticService.searchLink(searchIds,keyword);
            System.out.println(searchMeList.size());
            for(ElasticLink elasticLink:searchMeList){
                ObjectId linkOwnerId=new ObjectId(elasticLink.getOwnerId());
                String linkOwnerNickName=userRepository.findById(linkOwnerId).get().getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                Link link=folderService.linkRead(email,linkPath,linkId);
                myLinks.add(new ElasticLinkSearchInfo(linkOwnerId.toString(),linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
            }
        }
        else{
            List<String> followingIds=userRepository.findById(ownerId).get().getFollowers().stream().map(current->current.toString()).collect(Collectors.toList());
            List<ElasticLink> searchFollowingList= elasticService.searchLink(followingIds,keyword);
            followingIds.add(ownerId.toString());
            List<ElasticLink> searchNotFollowingList= elasticService.searchExcludeLink(followingIds,keyword);
            for(ElasticLink elasticLink:searchFollowingList){
                ObjectId linkOwnerId=new ObjectId(elasticLink.getOwnerId());
                String linkOwnerNickName=userRepository.findById(linkOwnerId).get().getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                Link link=folderService.linkRead(email,linkPath,linkId);
                followingLinks.add(new ElasticLinkSearchInfo(linkOwnerId.toString(),linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
            }
            for(ElasticLink elasticLink:searchNotFollowingList){
                ObjectId linkOwnerId=new ObjectId(elasticLink.getOwnerId());
                String linkOwnerNickName=userRepository.findById(linkOwnerId).get().getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                Link link=folderService.linkRead(email,linkPath,linkId);
                notFollowingLinks.add(new ElasticLinkSearchInfo(linkOwnerId.toString(),linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
            }
        }

        ElasticLinkSearchResponseDto elasticLinkSearchResponseDto = ElasticLinkSearchResponseDto.builder()
                .myLinks(myLinks)
                .followingLinks(followingLinks)
                .notFollowingLinks(notFollowingLinks)
                .build();

        return ResponseEntity.ok().body(elasticLinkSearchResponseDto);
    }




}
