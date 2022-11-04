package PoolC.Comect.image.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;


@Data
public class CreateImageRequestDto {
    @Email
    private String email;
    private MultipartFile multipartFile;

}
