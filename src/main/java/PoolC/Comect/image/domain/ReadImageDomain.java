package PoolC.Comect.image.domain;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class ReadImageDomain {
    private String contentType;
    private Resource resource;

    public ReadImageDomain(String contentType, Resource resource) {
        this.contentType = contentType;
        this.resource = resource;
    }
}
