package PoolC.Comect.image.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CreateImageRequestDto {
    private String email;
    private MultipartFile multipartFile;

}
