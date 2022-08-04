package PoolC.Comect.domain.data;

import lombok.Generated;
import org.springframework.data.annotation.Id;


public class Link {
    @Id
    @Generated
    private String id;

    private String name;
    private String picture;
    private String parentFolderId;
    private String link;

}
