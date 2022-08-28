package PoolC.Comect.folder.controller;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.dto.*;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.relation.controller.RelationController;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;
    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(RelationController.class);

    @PostMapping(value="/folder")
    public ResponseEntity<Void> folderCreate(@RequestBody FolderCreateRequestDto folderCreateRequestDto){
        logger.info(
                "Called POST/folder, \tbody: email="+folderCreateRequestDto.getEmail()+
                ", \tpath="+folderCreateRequestDto.getPath()+
                ", \tname="+folderCreateRequestDto.getName()
        );
        String email = folderCreateRequestDto.getEmail();
        String path = folderCreateRequestDto.getPath();
        String name = folderCreateRequestDto.getName();
        folderService.folderCreate(email,path,name);

        return ResponseEntity.ok().build();
    }
    @GetMapping(value="/folder")
    public ResponseEntity<FolderReadResponseDto> folderRead(@ModelAttribute FolderReadRequestDto folderReadRequestDto){
        logger.info(
                "Called GET/folder, \tparameter: email="+folderReadRequestDto.getEmail()+
                        ", \tpath="+folderReadRequestDto.getPath()+
                        ", \tshowLink="+folderReadRequestDto.getShowLink()
        );
        String email=folderReadRequestDto.getEmail();
        String path=folderReadRequestDto.getPath();
        String showFolder=folderReadRequestDto.getShowLink();
        Folder folder =folderService.folderRead(email,path);

        List<LinkInfo> linkInfos = new ArrayList<>();
        if(showFolder.equals("true")){
            linkInfos=LinkInfo.toLinkInfo(folder.getLinks());
        }

        return ResponseEntity.ok().body(FolderReadResponseDto.builder()
                .folderInfos(FolderInfo.toFolderInfo(folder.getFolders()))
                .linkInfos(linkInfos)
                .build());
    }

    @PutMapping(value="/folder")
    public ResponseEntity<Void> folderUpdate(@RequestBody FolderUpdateRequestDto folderUpdateRequestDto){
        logger.info(
                "Called PUT/folder, \tbody: email="+folderUpdateRequestDto.getEmail()+
                        ", \tpath="+folderUpdateRequestDto.getPath()+
                        ", \tshowLink="+folderUpdateRequestDto.getNewName()
        );
        String email = folderUpdateRequestDto.getEmail();
        String path = folderUpdateRequestDto.getPath();
        String newName = folderUpdateRequestDto.getNewName();
        folderService.folderUpdate(email,path,newName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/folder")
    public ResponseEntity<Void> folderDelete(@RequestBody FolderDeleteRequestDto folderDeleteRequestDto){
        logger.info(
                "Called DELETE/folder, \tbody: email="+folderDeleteRequestDto.getEmail()+
                        ", \tpaths="+folderDeleteRequestDto.getPaths()
        );
        String email = folderDeleteRequestDto.getEmail();
        List<String> paths = folderDeleteRequestDto.getPaths();
        folderService.folderDelete(email,paths);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/folder/path")
    public ResponseEntity<Void> folderMove(@RequestBody FolderMoveRequestDto folderMoveRequestDto){
        logger.info(
                "Called PUT/folder/path, \tbody: email="+folderMoveRequestDto.getEmail()+
                        ", \toriginalPaths="+folderMoveRequestDto.getOriginalPaths()+
                        ", \tmodifiedPath="+folderMoveRequestDto.getModifiedPath()
        );
        String email=folderMoveRequestDto.getEmail();
        List<String> originalPaths=folderMoveRequestDto.getOriginalPaths();
        String modifiedPath=folderMoveRequestDto.getModifiedPath();
        folderService.folderMove(email,originalPaths,modifiedPath);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/folder/path")
    public ResponseEntity<FolderCheckPathResponseDto> folderCheckPath(@ModelAttribute FolderCheckPathRequestDto folderCheckPathRequestDto){
        logger.info(
                "Called GET/folder/path, \tparameter: email="+folderCheckPathRequestDto.getEmail()+
                        ", \tpath="+folderCheckPathRequestDto.getPath()
        );
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
    public ResponseEntity<Void> linkCreate(@RequestBody LinkCreateRequestDto linkCreateRequestDto){
        logger.info(
                "Called POST/link, \tbody: email="+linkCreateRequestDto.getEmail()+
                        ", \tpath="+linkCreateRequestDto.getPath()+
                        ", \tname="+linkCreateRequestDto.getName()+
                        ", \turl="+linkCreateRequestDto.getUrl()+
                        ", \timageUrl="+linkCreateRequestDto.getImageUrl()+
                        ", \tkeywords="+linkCreateRequestDto.getKeywords()+
                        ", \tisPublic="+linkCreateRequestDto.getIsPublic()
        );
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
    public ResponseEntity<Void> linkUpdate(@RequestBody LinkUpdateRequestDto linkUpdateRequestDto){
        logger.info(
                "Called PUT/link, \tbody: id="+linkUpdateRequestDto.getId() +
                        "\temail="+linkUpdateRequestDto.getEmail()+
                        ", \tpath="+linkUpdateRequestDto.getPath()+
                        ", \tname="+linkUpdateRequestDto.getName()+
                        ", \turl="+linkUpdateRequestDto.getUrl()+
                        ", \timageUrl="+linkUpdateRequestDto.getImageUrl()+
                        ", \tkeywords="+linkUpdateRequestDto.getKeywords()+
                        ", \tisPublic="+linkUpdateRequestDto.getIsPublic()
        );
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
    public ResponseEntity<Void> linkDelete(@RequestBody LinkDeleteRequestDto linkDeleteRequestDto){
        logger.info(
                "Called DELETE/link, \tbody: email="+linkDeleteRequestDto.getEmail()+
                        ", \tpaths="+linkDeleteRequestDto.getPaths()+
                        ", \tids="+linkDeleteRequestDto.getIds()
        );
        String email=linkDeleteRequestDto.getEmail();
        List<String> paths=linkDeleteRequestDto.getPaths();
        List<String> ids=linkDeleteRequestDto.getIds();
        folderService.linkDelete(email,paths,ids);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/link/path")
    public ResponseEntity<Void> linkMove(@RequestBody LinkMoveRequestDto linkMoveRequestDto){
        logger.info(
                "Called PUT/link/path, \tbody: email="+linkMoveRequestDto.getEmail()+
                        ", \toriginalPaths="+linkMoveRequestDto.getOriginalPaths()+
                        ", \toriginalIds="+linkMoveRequestDto.getOriginalIds()+
                        ", \tmodifiedPath="+linkMoveRequestDto.getModifiedPath()
        );
        String email=linkMoveRequestDto.getEmail();
        List<String> originalPaths=linkMoveRequestDto.getOriginalPaths();
        List<String> originalIds=linkMoveRequestDto.getOriginalIds();
        String modifiedPath=linkMoveRequestDto.getModifiedPath();
        folderService.linkMove(email,originalPaths,originalIds,modifiedPath);
        return ResponseEntity.ok().build();
    }

}
