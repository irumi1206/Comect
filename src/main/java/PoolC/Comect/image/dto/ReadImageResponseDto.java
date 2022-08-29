package PoolC.Comect.image.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReadImageResponseDto {
    private Resource image;

}
