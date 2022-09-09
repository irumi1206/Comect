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
    private final UserService userService;
    private final FolderService folderService;

    @GetMapping(value = "/folder/elastic")
    public ResponseEntity<ElasticFolderSearchResponseDto> folderSearch(@ModelAttribute ElasticFolderSearchRequestDto elasticFolderSearchRequestDto) {

        String email=elasticFolderSearchRequestDto.getEmail();
        String keyword=elasticFolderSearchRequestDto.getKeyword();
        String searchMe=elasticFolderSearchRequestDto.getSearchMe();

        User user=userService.findOneEmail(email);
        ObjectId ownerId=user.getId();

        List<ElasticFolderSearchInfo> myFolders=new ArrayList<>();
        List<ElasticFolderSearchInfo> followingFolders=new ArrayList<>();
        List<ElasticFolderSearchInfo> notFollowingFolders=new ArrayList<>();

        if(searchMe.equals("true")){
            List<String> searchIds=new ArrayList<>();
            searchIds.add(ownerId.toString());
            List<ElasticFolder> searchMeList= elasticService.searchFolder(searchIds,keyword);
            for(ElasticFolder elasticFolder:searchMeList){
                String folderOwnerId=elasticFolder.getOwnerId();
                String folderOwnerNickname=userService.findOneId(new ObjectId(folderOwnerId)).getNickname();
                String folderOwnerEmail=userService.findOneId(new ObjectId(folderOwnerId)).getEmail();
                myFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname,folderOwnerEmail));
            }
        }
        else{
            List<String> followingIds=userService.readFollowingById(ownerId).stream().map(current->current.toString()).collect(Collectors.toList());
            System.out.println(followingIds);
            List<ElasticFolder> searchFollowingList= elasticService.searchFolder(followingIds,keyword);
            followingIds.add(ownerId.toString());
            List<ElasticFolder> searchNotFollowingList= elasticService.searchExcludeFolder(followingIds,keyword);
            for(ElasticFolder elasticFolder:searchFollowingList){
                String folderOwnerId=elasticFolder.getOwnerId();
                String folderOwnerNickname=userService.findOneId(new ObjectId(folderOwnerId)).getNickname();
                String folderOwnerEmail=userService.findOneId(new ObjectId(folderOwnerId)).getEmail();
                followingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname,folderOwnerEmail));
            }
            for(ElasticFolder elasticFolder:searchNotFollowingList){
                String folderOwnerId=elasticFolder.getOwnerId();
                String folderOwnerNickname=userService.findOneId(new ObjectId(folderOwnerId)).getNickname();
                String folderOwnerEmail=userService.findOneId(new ObjectId(folderOwnerId)).getEmail();
                notFollowingFolders.add(ElasticFolderSearchInfo.toElasticFolderSearchInfo(elasticFolder,folderOwnerNickname,folderOwnerEmail));
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

        User user=userService.findOneEmail(email);
        ObjectId ownerId=user.getId();

        List<ElasticLinkSearchInfo> myLinks=new ArrayList<>();
        List<ElasticLinkSearchInfo> followingLinks=new ArrayList<>();
        List<ElasticLinkSearchInfo> notFollowingLinks=new ArrayList<>();

        if(searchMe.equals("true")){
            List<ElasticLink> searchMeList= elasticService.searchLinkMe(ownerId.toString(),keyword);
            System.out.println(searchMeList.size());
            for(ElasticLink elasticLink:searchMeList){
                String linkOwnerId=elasticLink.getOwnerId();
                String linkOwnerNickName=userService.findOneId(new ObjectId(linkOwnerId)).getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                String linkEmail=userService.findOneId(new ObjectId(linkOwnerId)).getEmail();
                Link link=folderService.linkRead(linkEmail,linkPath,linkId);
                myLinks.add(new ElasticLinkSearchInfo(linkOwnerId,linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
            }
        }
        else{
            List<String> followingIds=userService.findOneId(ownerId).getFollowers().stream().map(current->current.toString()).collect(Collectors.toList());
            List<ElasticLink> searchFollowingList= elasticService.searchLink(followingIds,keyword);
            followingIds.add(ownerId.toString());
            List<ElasticLink> searchNotFollowingList= elasticService.searchExcludeLink(followingIds,keyword);
            System.out.println(searchFollowingList.size());
            System.out.println(searchNotFollowingList.size());
            for(ElasticLink elasticLink:searchFollowingList){
                String linkOwnerId=elasticLink.getOwnerId();
                String linkOwnerNickName=userService.findOneId(new ObjectId(linkOwnerId)).getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                String linkEmail=userService.findOneId(new ObjectId(linkOwnerId)).getEmail();
                Link link=folderService.linkRead(linkEmail,linkPath,linkId);
                followingLinks.add(new ElasticLinkSearchInfo(linkOwnerId,linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
            }
            for(ElasticLink elasticLink:searchNotFollowingList){
                String linkOwnerId=elasticLink.getOwnerId();
                String linkOwnerNickName=userService.findOneId(new ObjectId(linkOwnerId)).getNickname();
                String linkPath=elasticLink.getPath();
                String linkName=elasticLink.getLinkName();
                String linkId=elasticLink.getLinkId();
                String linkEmail=userService.findOneId(new ObjectId(linkOwnerId)).getEmail();
                Link link=folderService.linkRead(linkEmail,linkPath,linkId);
                notFollowingLinks.add(new ElasticLinkSearchInfo(linkOwnerId,linkOwnerNickName,linkPath,linkId,linkName,link.getImageUrl(),link.getUrl(),link.getKeywords()));
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
