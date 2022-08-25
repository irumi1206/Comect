package PoolC.Comect.folder.controller;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.dto.*;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;
    private final UserRepository userRepository;

    @PostMapping(value="/folder")
    public ResponseEntity<Void> folderCreate(@RequestBody FolderCreateRequestDto folderCreateRequestDto){
        String email = folderCreateRequestDto.getEmail();
        String path = folderCreateRequestDto.getPath();
        String name = folderCreateRequestDto.getName();
        folderService.folderCreate(email,path,name);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/folder")
    public ResponseEntity<FolderReadResponseDto> folderRead(@ModelAttribute FolderReadRequestDto folderReadRequestDto){
        String email=folderReadRequestDto.getEmail();
        String path=folderReadRequestDto.getPath();
        Folder folder =folderService.folderRead(email,path);
        FolderReadResponseDto folderReadResponseDto = FolderReadResponseDto.builder()
                .folderInfos(FolderInfo.toFolderInfo(folder.getFolders()))
                .linkInfos(LinkInfo.toLinkInfo(folder.getLinks()))
                .build();

        return ResponseEntity.ok().body(folderReadResponseDto);
    }

    @GetMapping(value = "/folder/folder")
    public ResponseEntity<FolderReadFolderResponseDto> folderReadFolder(@ModelAttribute FolderReadFolderRequestDto folderReadFolderRequestDto){
        String email = folderReadFolderRequestDto.getEmail();
        String path=folderReadFolderRequestDto.getPath();
        Folder folder=folderService.folderRead(email,path);
        FolderReadFolderResponseDto folderReadFolderResponseDto = FolderReadFolderResponseDto.builder()
                .folderInfos(FolderInfo.toFolderInfo(folder.getFolders()))
                .build();

        return ResponseEntity.ok().body(folderReadFolderResponseDto);
    }

    @PutMapping(value="/folder")
    public ResponseEntity<Void> folderUpdate(@RequestBody FolderUpdateRequestDto folderUpdateRequestDto){
        String email = folderUpdateRequestDto.getEmail();
        String path = folderUpdateRequestDto.getPath();
        String newName = folderUpdateRequestDto.getNewName();
        folderService.folderUpdate(email,path,newName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/folder")
    public ResponseEntity<Void> folderDelete(@RequestBody FolderDeleteRequestDto folderDeleteRequestDto){
        String email = folderDeleteRequestDto.getEmail();
        String path = folderDeleteRequestDto.getPath();
        folderService.folderDelete(email,path);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/folder/path")
    public ResponseEntity<Void> folderMove(@RequestBody FolderMoveRequestDto folderMoveRequestDto){
        String email=folderMoveRequestDto.getEmail();
        List<String> originalPaths=folderMoveRequestDto.getOriginalPaths();
        String modifiedPath=folderMoveRequestDto.getModifiedPath();
        folderService.folderMove(email,originalPaths,modifiedPath);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/folder/path")
    public ResponseEntity<FolderCheckPathResponseDto> folderCheckPath(@ModelAttribute FolderCheckPathRequestDto folderCheckPathRequestDto){
        String email = folderCheckPathRequestDto.getEmail();
        String path = folderCheckPathRequestDto.getPath();
        boolean checkValid = folderService.folderCheckPath(email,path);
        int valid = checkValid? 1 :0;
        FolderCheckPathResponseDto folderCheckPathResponseDto=FolderCheckPathResponseDto.builder()
                .valid(valid)
                .build();
        return ResponseEntity.ok().body(folderCheckPathResponseDto);
    }

    @PostMapping(value="/link")
    public ResponseEntity<LinkCreateRequestDto> linkCreate(@RequestBody LinkCreateRequestDto linkCreateRequestDto){
        String email=linkCreateRequestDto.getEmail();
        String path=linkCreateRequestDto.getPath();
        String name=linkCreateRequestDto.getName();
        String url=linkCreateRequestDto.getUrl();
        String imageUrl=linkCreateRequestDto.getImageUrl();
        List<String> keywords=linkCreateRequestDto.getKeywords();
        String isPublic = linkCreateRequestDto.getIsPublic();
        folderService.linkCreate(email,path,name,url,imageUrl,keywords,isPublic);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/link")
    public ResponseEntity<LinkUpdateRequestDto> linkUpdate(@RequestBody LinkUpdateRequestDto linkUpdateRequestDto){
        String id=linkUpdateRequestDto.getId();
        String email=linkUpdateRequestDto.getEmail();
        String path=linkUpdateRequestDto.getPath();
        String name=linkUpdateRequestDto.getName();
        String url=linkUpdateRequestDto.getUrl();
        String imageUrl=linkUpdateRequestDto.getImageUrl();
        List<String> keywords=linkUpdateRequestDto.getKeywords();
        String isPublic=linkUpdateRequestDto.getIsPublic();
        folderService.linkUpdate(id,email,path,name,url,imageUrl,keywords,isPublic);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/link")
    public ResponseEntity<LinkDeleteRequestDto> linkDelete(@RequestBody LinkDeleteRequestDto linkDeleteRequestDto){
        String email=linkDeleteRequestDto.getEmail();
        String path=linkDeleteRequestDto.getPath();
        String id=linkDeleteRequestDto.getId();
        folderService.linkDelete(email,path,id);
        return ResponseEntity.ok().build();
    }

}
