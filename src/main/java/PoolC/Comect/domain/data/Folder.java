package PoolC.Comect.domain.data;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
public class Folder {
    @Id
    @Generated
    private String id;
    private String name;
    private String picture;
    private String parentFolderId;
    private List<Folder> folders;
    private List<Link> links;
}
