package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Folder;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class FolderInfo {

    private String folderName;

    @Builder
    public FolderInfo(String folderName){
        this.folderName=folderName;
    }

    @Override
    public String toString() {
        return "FolderInfo{" +
                "folderName='" + folderName + '\'' +
                '}';
    }

    public static List<FolderInfo> toFolderInfo(List<String> folderNames){

        List<FolderInfo> folderInfos=new ArrayList<>();

        for(int i=0;i<folderNames.size();++i){

            FolderInfo temp=FolderInfo.builder()
                    .folderName(folderNames.get(i))
                    .build();

            folderInfos.add(temp);
        }

        return folderInfos;
    }


}
