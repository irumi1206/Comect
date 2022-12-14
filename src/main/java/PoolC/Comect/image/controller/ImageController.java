package PoolC.Comect.image.controller;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.image.domain.ReadImageDomain;
import PoolC.Comect.image.dto.*;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.ImageUploadData;
import PoolC.Comect.user.domain.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    @Autowired
    private ImageService imageService;

//    @ApiOperation(value="이미지 추가", notes="")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
//            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
//            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
//            @ApiResponse(responseCode = "404", description = "해당 이메일의 유저가 없을때"),
//    })
//    @PostMapping("/image")
//    public ResponseEntity<CreateImageResponseDto> createImage(@ModelAttribute CreateImageRequestDto createImageRequestDto){
//        ImageUploadData imageUpLoadData = imageService.createImage(createImageRequestDto.getMultipartFile(), createImageRequestDto.getEmail());
//        CreateImageResponseDto createImageResponseDto = new CreateImageResponseDto(imageUpLoadData.isSuccess());
//        return ResponseEntity.ok(createImageResponseDto);
//    }

    @ApiOperation(value="이미지 조회", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 아이디의 파일이 없음"),
    })
    @GetMapping("/image")
    public ResponseEntity<Resource> readImage(@ModelAttribute ReadImageRequestDto readImageRequestDto){
        ReadImageResponseDto readImageResponseDto = new ReadImageResponseDto();
        ObjectId id = validateId(readImageRequestDto.getId());
        ReadImageDomain readImageDomain = imageService.readImage(id);
        readImageResponseDto.setImage(readImageDomain.getResource());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("image", StandardCharsets.UTF_8)
                        .build()
        );
        headers.add(HttpHeaders.CONTENT_TYPE, readImageDomain.getContentType());
        return new ResponseEntity<>(readImageDomain.getResource(),headers, HttpStatus.OK);
    }

    @ApiOperation(value="이미지 삭제", notes="")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 잘 생성됨."),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(responseCode = "401", description = "쿠키가 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "해당 아이디의 파일이 없음"),
    })
    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@ModelAttribute DeleteImageRequestDto deleteImageRequestDto, @AuthenticationPrincipal User user){
        ObjectId id = validateId(deleteImageRequestDto.getId());
        imageService.deleteImage(id,user.getId().toString());
        return ResponseEntity.ok().build();
    }

    private ObjectId validateId(String id){
        ObjectId objectId;
        try{
            objectId = new ObjectId(id);
        }catch(Exception e){
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
        return objectId;
    }

}
