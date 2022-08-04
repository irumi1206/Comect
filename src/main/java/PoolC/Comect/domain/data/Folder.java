package PoolC.Comect.domain.data;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
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
    private List<Folder> folders = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public Folder(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public void addFolder(Folder folder){
        this.folders.add(folder);
    }
    public void addLink(Link link){
        this.links.add(link);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", parentFolderName='" + parentFolderId + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
