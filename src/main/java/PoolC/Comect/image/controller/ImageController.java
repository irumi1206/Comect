package PoolC.Comect.image.controller;

import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.dto.CreateImageRequestDto;
import PoolC.Comect.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class ImageController {
    private ImageService imageService;

    @PostMapping("/image")
    public String saveImage(@RequestParam("image")CreateImageRequestDto createImageRequestDto) throws IOException {
        String uploadDir = imageService.save(createImageRequestDto.getImageName(), createImageRequestDto.getMultipartFile());
        return uploadDir;
    }
}
