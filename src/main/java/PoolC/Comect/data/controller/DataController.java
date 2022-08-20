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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;
    private final UserRepository userRepository;

    @PostMapping("/folder/create")
    public ResponseEntity<Void> folderCreate(@RequestBody FolderCreateRequestDto folderCreateRequestDto)throws IllegalAccessException{
//        String userEmail = folderCreateRequestDto.getUserEmail();
//        String path = folderCreateRequestDto.getPath();
//        String folderName = folderCreateRequestDto.getFolderName();
//        dataService.folderCreate(userEmail,path,folderName);
//        Data user1Data = new Data();
//        User user1 = new User("user1", "user1Email@email.com", user1Data.getId(), "user1Picture", "user1");
//        //dataRepository.save(user1Data);
//        userRepository.save(user1);
        log.trace("folder create success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/folder/read")
    public ResponseEntity<FolderReadResponseDto> folderRead(@RequestBody FolderReadRequestDto folderReadRequestDto) throws IllegalAccessException{
        String userEmail=folderReadRequestDto.getUserEmail();
        String path=folderReadRequestDto.getPath();
        List<String> folderNames= dataService.folderReadFolder(userEmail,path);
        List<Link> links=dataService.folderReadLink(userEmail,path);
        return ResponseEntity.ok().body(FolderReadResponseDto.builder()
                .folderNames(folderNames)
                .links(links)
                .build());
    }

    @PostMapping("folder/readFolder")
    public ResponseEntity<FolderReadFolderResponseDto> folderReadFolder(@RequestBody FolderReadFolderRequestDto folderReadFolderRequestDto) throws IllegalAccessException{
        String userEmail = folderReadFolderRequestDto.getUserEmail();
        String path=folderReadFolderRequestDto.getPath();
        List<String> folderNames = dataService.folderReadFolder(userEmail,path);
        return ResponseEntity.ok().body(FolderReadFolderResponseDto.builder()
                .folderNames(folderNames)
                .build());
    }

    @PostMapping("folder/update")
    public ResponseEntity<Void> folderUpdate(@RequestBody FolderUpdateRequestDto folderUpdateRequestDto) throws IllegalAccessException{
        String userEmail = folderUpdateRequestDto.getUserEmail();
        String path = folderUpdateRequestDto.getPath();
        String folderName = folderUpdateRequestDto.getFolderName();
        dataService.folderUpdate(userEmail,path,folderName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("folder/delete")
    public ResponseEntity<Void> folderDelete(@RequestBody FolderDeleteRequestDto folderDeleteRequestDto) throws IllegalAccessException{
        String userEmail = folderDeleteRequestDto.getUserEmail();
        String path = folderDeleteRequestDto.getPath();
        dataService.folderDelete(userEmail,path);
        return ResponseEntity.ok().build();
    }

    @PostMapping("folder/move")
    public ResponseEntity<Void> folderMove(@RequestBody FolderMoveRequestDto folderMoveRequestDto) throws IllegalAccessException{
        String userEmail=folderMoveRequestDto.getUserEmail();
        String originalPath=folderMoveRequestDto.getOriginalPath();
        String modifiedPath=folderMoveRequestDto.getModifiedPath();
        dataService.folderMove(userEmail,originalPath,modifiedPath);
        return ResponseEntity.ok().build();
    }


}
