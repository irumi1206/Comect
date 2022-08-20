package PoolC.Comect.data.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
public class Folder {

    private String name;
    private List<Folder> folders;
    private List<Link> links;

    public Folder(String name) {
        this.name = name;
        folders=new ArrayList<>();
        links=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Folder{" +
                ", name='" + name + '\'' +
                ", folders=" + folders +
                ", links=" + links +
                '}';
    }
}
