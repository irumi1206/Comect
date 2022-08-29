package PoolC.Comect.image.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Data
public class CreateImageRequestDto {
    private String email;
    private MultipartFile multipartFile;
    private String imageName;

}
