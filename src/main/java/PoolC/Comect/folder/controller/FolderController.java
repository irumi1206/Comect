package PoolC.Comect.folder.controller;
import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.dto.*;
import PoolC.Comect.folder.service.FolderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;

    @ApiOperation(value="폴더 추가", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "폴더 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "형식이 잘못됨(폴더이름)"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음"),
            @ApiResponse(responseCode = "409", description = "이미 동일한 이름의 폴더가 존재함")
    })
    @PostMapping(value="/folder")
    public ResponseEntity<Void> folderCreate(@Valid @RequestBody FolderCreateRequestDto folderCreateRequestDto){

        String email = folderCreateRequestDto.getEmail();
        String path = folderCreateRequestDto.getPath();
        String name = folderCreateRequestDto.getName();
        folderService.folderCreate(email,path,name);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="폴더 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "폴더 잘 조회됨."),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음"),
    })
    @GetMapping(value="/folder")
    public ResponseEntity<FolderReadResponseDto> folderRead(@ModelAttribute FolderReadRequestDto folderReadRequestDto){

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

    @ApiOperation(value="폴더 수정", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "폴더 잘 수정됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 형식(폴더이름),경로가 루트임"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음"),
            @ApiResponse(responseCode = "409", description = "이미 해당 폴더이름이 존재함")
    })
    @PutMapping(value="/folder")
    public ResponseEntity<Void> folderUpdate(@Valid @RequestBody FolderUpdateRequestDto folderUpdateRequestDto){
        String email = folderUpdateRequestDto.getEmail();
        String path = folderUpdateRequestDto.getPath();
        String newName = folderUpdateRequestDto.getNewName();
        folderService.folderUpdate(email,path,newName);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="폴더 삭제", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "폴더 잘 삭제됨."),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음,경로가 루트임"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @DeleteMapping(value="/folder")
    public ResponseEntity<Void> folderDelete(@RequestBody FolderDeleteRequestDto folderDeleteRequestDto){
        String email = folderDeleteRequestDto.getEmail();
        List<String> paths = folderDeleteRequestDto.getPaths();
        folderService.folderDelete(email,paths);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="폴더 이동", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "폴더 잘 이동됨."),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음,originalpath중 루트폴더가 있음elas"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음"),
            @ApiResponse(responseCode = "409", description = "이미 폴더가 존재함")
    })
    @PutMapping(value="/folder/path")
    public ResponseEntity<Void> folderMove(@RequestBody FolderMoveRequestDto folderMoveRequestDto){
        String email=folderMoveRequestDto.getEmail();
        List<String> originalPaths=folderMoveRequestDto.getOriginalPaths();
        String modifiedPath=folderMoveRequestDto.getModifiedPath();
        folderService.folderMove(email,originalPaths,modifiedPath);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="폴더 경로 유효성", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "1이면 가능, 0이면 경로 불가능"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음")
    })
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

    @ApiOperation(value="링크 추가", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "링크 잘 추가됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식(링크이름,키워드이름)"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @PostMapping(value="/link")
    public ResponseEntity<LinkCreateResponseDto> linkCreate(@Valid @ModelAttribute LinkCreateRequestDto linkCreateRequestDto){
        String email=linkCreateRequestDto.getEmail();
        String path=linkCreateRequestDto.getPath();
        String name=linkCreateRequestDto.getName();
        String url=linkCreateRequestDto.getUrl();
        MultipartFile multipartFile = linkCreateRequestDto.getMultipartFile();
        List<String> keywords=linkCreateRequestDto.getKeywords();
        for(String keyword:keywords) if(keyword.length()>30 || keyword.contains(" ") ) throw new CustomException(ErrorCode.INVALID_KEYWORD);
        String isPublic = linkCreateRequestDto.getIsPublic();
        boolean imageSuccess = folderService.linkCreate(email, path, name, url, multipartFile, keywords, isPublic);
        LinkCreateResponseDto linkCreateResponseDto = new LinkCreateResponseDto();
        linkCreateResponseDto.setImageSuccess(imageSuccess);
        return ResponseEntity.ok(linkCreateResponseDto);
    }

    @ApiOperation(value="링크 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "링크 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식(링크이름,키워드이름)"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @GetMapping(value="/link")
    public ResponseEntity<LinkReadResponseDto> linkRead(@ModelAttribute LinkReadRequestDto linkReadRequestDto){

        String email=linkReadRequestDto.getEmail();
        String path=linkReadRequestDto.getPath();
        String id=linkReadRequestDto.getId();
        Link link=folderService.linkRead(email,path,id);

        return ResponseEntity.ok().body(LinkReadResponseDto.builder()
                .id(link.get_id().toString())
                .name(link.getName())
                .imageUrl(link.getImageUrl())
                .url(link.getUrl())
                .keywords(link.getKeywords())
                .isPublic(link.getIsPublic())
                .build());
    }

    @ApiOperation(value="링크 수정", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "링크 잘 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식(링크이름,키워드이름)"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @PutMapping(value="/link")
    public ResponseEntity<LinkUpdateResponseDto> linkUpdate(@Valid @ModelAttribute LinkUpdateRequestDto linkUpdateRequestDto){
        String id=linkUpdateRequestDto.getId();
        String email=linkUpdateRequestDto.getEmail();
        String path=linkUpdateRequestDto.getPath();
        String name=linkUpdateRequestDto.getName();
        String url=linkUpdateRequestDto.getUrl();
        MultipartFile multipartFile=linkUpdateRequestDto.getMultipartFile();
        List<String> keywords=linkUpdateRequestDto.getKeywords();
        String isPublic=linkUpdateRequestDto.getIsPublic();
        String imageChage = linkUpdateRequestDto.getImageChange();
        boolean imageSuccess = folderService.linkUpdate(id, email, path, name, url, multipartFile, keywords, isPublic, imageChage);

        LinkUpdateResponseDto linkUpdateResponseDto = new LinkUpdateResponseDto();
        linkUpdateResponseDto.setImageSuccess(imageSuccess);
        return ResponseEntity.ok(linkUpdateResponseDto);
    }

    @ApiOperation(value="링크 삭제", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "링크 잘 삭제됨"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @DeleteMapping(value="/link")
    public ResponseEntity<Void> linkDelete(@RequestBody LinkDeleteRequestDto linkDeleteRequestDto){
        String email=linkDeleteRequestDto.getEmail();
        String path=linkDeleteRequestDto.getPath();
        List<String> ids=linkDeleteRequestDto.getIds();
        folderService.linkDelete(email,path,ids);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="링크 이동", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "링크 잘 수정됨"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "경로를 못찾음")
    })
    @PutMapping(value="/link/path")
    public ResponseEntity<Void> linkMove(@RequestBody LinkMoveRequestDto linkMoveRequestDto){
        String email=linkMoveRequestDto.getEmail();
        String originalPath=linkMoveRequestDto.getOriginalPath();
        List<String> originalIds=linkMoveRequestDto.getOriginalIds();
        String modifiedPath=linkMoveRequestDto.getModifiedPath();
        folderService.linkMove(email,originalPath,originalIds,modifiedPath);
        return ResponseEntity.ok().build();
    }

}
