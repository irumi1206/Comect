package PoolC.Comect.folder.dto;

import PoolC.Comect.folder.domain.Folder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class FolderInfo {

    private String name;

    @Builder
    public FolderInfo(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "FolderInfo{" +
                "name='" + name + '\'' +
                '}';
    }

    public static List<FolderInfo> toFolderInfo(List<Folder> folders){

        List<FolderInfo> folderInfos=new ArrayList<>();

        for(Folder currentFolder : folders){

            folderInfos.add(FolderInfo.builder()
                    .name(currentFolder.getName())
                    .build());

        }

        return folderInfos;
    }


}
