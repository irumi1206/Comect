package PoolC.Comect.image.domain;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class ReadImageDomain {
    private String contentType;
    private Resource resource;
    private String imageName;

    public ReadImageDomain(String contentType, Resource resource, String imageName) {
        this.contentType = contentType;
        this.resource = resource;
        this.imageName = imageName;
    }
}
