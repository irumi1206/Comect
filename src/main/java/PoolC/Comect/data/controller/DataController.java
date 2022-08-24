package PoolC.Comect.data.controller;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.domain.Link;
import PoolC.Comect.data.dto.*;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.data.service.DataService;
import PoolC.Comect.relation.dto.ReadRelationResponseDto;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;
    private final UserRepository userRepository;

    @PostMapping("/folder/create")
    public ResponseEntity<Void> folderCreate(@RequestBody FolderCreateRequestDto folderCreateRequestDto){
        String userEmail = folderCreateRequestDto.getUserEmail();
        String path = folderCreateRequestDto.getPath();
        String folderName = folderCreateRequestDto.getFolderName();
        dataService.folderCreate(userEmail,path,folderName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/folder/checkPath")
    public ResponseEntity<FolderCheckPathResponseDto> folderCheckPath(@RequestBody FolderCheckPathRequestDto folderCheckPathRequestDto){
        String userEmail = folderCheckPathRequestDto.getUserEmail();
        String path = folderCheckPathRequestDto.getPath();
        int checkValid = dataService.folderCheckPath(userEmail,path);
        FolderCheckPathResponseDto folderCheckPathResponseDto=FolderCheckPathResponseDto.builder()
                .valid(checkValid)
                .build();
        return ResponseEntity.ok().body(folderCheckPathResponseDto);
    }

    @PostMapping("/folder/read")
    public ResponseEntity<FolderReadResponseDto> folderRead(@RequestBody FolderReadRequestDto folderReadRequestDto){
        String userEmail=folderReadRequestDto.getUserEmail();
        String path=folderReadRequestDto.getPath();
        List<FolderInfo> folderInfos= dataService.folderReadFolder(userEmail,path);
        System.out.println(dataService.folderReadLink(userEmail,path));
        List<LinkInfo> linkInfos=dataService.folderReadLink(userEmail,path);
        return ResponseEntity.ok().body(FolderReadResponseDto.builder()
                .folderInfos(folderInfos)
                .linkInfos(linkInfos)
                .build());
    }

    @PostMapping(value = "folder/readFolder")
    public ResponseEntity<FolderReadFolderResponseDto> folderReadFolder(@RequestBody FolderReadFolderRequestDto folderReadFolderRequestDto){
        String userEmail = folderReadFolderRequestDto.getUserEmail();
        String path=folderReadFolderRequestDto.getPath();
        List<FolderInfo> folderInfos = dataService.folderReadFolder(userEmail,path);
        FolderReadFolderResponseDto folderReadFolderResponseDto = FolderReadFolderResponseDto.builder().folderInfos(folderInfos).build();
        System.out.println(folderReadFolderResponseDto);
        return ResponseEntity.ok().body(folderReadFolderResponseDto);
    }

    @PostMapping("folder/update")
    public ResponseEntity<Void> folderUpdate(@RequestBody FolderUpdateRequestDto folderUpdateRequestDto){
        String userEmail = folderUpdateRequestDto.getUserEmail();
        String path = folderUpdateRequestDto.getPath();
        String folderName = folderUpdateRequestDto.getFolderName();
        dataService.folderUpdate(userEmail,path,folderName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("folder/delete")
    public ResponseEntity<Void> folderDelete(@RequestBody FolderDeleteRequestDto folderDeleteRequestDto){
        String userEmail = folderDeleteRequestDto.getUserEmail();
        String path = folderDeleteRequestDto.getPath();
        dataService.folderDelete(userEmail,path);
        return ResponseEntity.ok().build();
    }

    @PostMapping("folder/move")
    public ResponseEntity<Void> folderMove(@RequestBody FolderMoveRequestDto folderMoveRequestDto){
        String userEmail=folderMoveRequestDto.getUserEmail();
        String originalPath=folderMoveRequestDto.getOriginalPath();
        String modifiedPath=folderMoveRequestDto.getModifiedPath();
        dataService.folderMove(userEmail,originalPath,modifiedPath);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/link/create")
    public ResponseEntity<Void> linkCreate(@RequestBody LinkCreateRequestDto linkCreateRequestDto){
        String userEmail = linkCreateRequestDto.getUserEmail();
        String path = linkCreateRequestDto.getPath();
        String linkTitle = linkCreateRequestDto.getLinkTitle();
        String linkImage = linkCreateRequestDto.getLinkImage();
        String linkUrl = linkCreateRequestDto.getLinkUrl();
        List<String> keywords = linkCreateRequestDto.getKeywords();
        String isPublic = linkCreateRequestDto.getIsPublic();
        dataService.linkCreate(userEmail,path,linkTitle,linkImage,linkUrl,keywords,isPublic);
        return ResponseEntity.ok().build();
    }


}
