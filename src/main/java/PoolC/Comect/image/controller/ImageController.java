package PoolC.Comect.image.controller;

import PoolC.Comect.image.domain.ReadImageDomain;
import PoolC.Comect.image.dto.*;
import PoolC.Comect.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<CreateImageResponseDto> createImage(@ModelAttribute CreateImageRequestDto createImageRequestDto){
        String id=imageService.upLoad(createImageRequestDto.getImageName(), createImageRequestDto.getMultipartFile(), createImageRequestDto.getEmail()).toHexString();
        CreateImageResponseDto createImageResponseDto = new CreateImageResponseDto("image?id="+id);
        return ResponseEntity.ok(createImageResponseDto);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> readImage(@ModelAttribute ReadImageRequestDto readImageRequestDto){
        ReadImageResponseDto readImageResponseDto = new ReadImageResponseDto();
        ReadImageDomain readImageDomain = imageService.readImage(new ObjectId(readImageRequestDto.getId()));
        readImageResponseDto.setImage(readImageDomain.getResource());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(readImageDomain.getImageName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.add(HttpHeaders.CONTENT_TYPE, readImageDomain.getContentType());
        return new ResponseEntity<>(readImageDomain.getResource(),headers, HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@ModelAttribute DeleteImageRequestDto deleteImageRequestDto){
        imageService.deleteImage(new ObjectId(deleteImageRequestDto.getId()));
        return ResponseEntity.ok().build();
    }

}
