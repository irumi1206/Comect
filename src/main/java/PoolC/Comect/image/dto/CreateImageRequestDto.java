package PoolC.Comect.image.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateImageRequestDto {
    private MultipartFile multipartFile;
    private String imageName;

    public CreateImageRequestDto(MultipartFile multipartFile, String imageName) {
        this.multipartFile = multipartFile;
        this.imageName = imageName;
    }
}
