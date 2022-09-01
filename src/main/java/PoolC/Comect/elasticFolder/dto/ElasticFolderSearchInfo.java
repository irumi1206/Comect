//package PoolC.Comect.elasticFolder.dto;
//
//import PoolC.Comect.elasticFolder.domain.ElasticFolder;
//import PoolC.Comect.folder.domain.Folder;
//import PoolC.Comect.folder.dto.FolderInfo;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//public class ElasticFolderSearchInfo {
//
//    private String nickName;
//    private String path;
//    private String name;
//
//    @Builder
//    public ElasticFolderSearchInfo(String nickName, String path, String name){
//        this.nickName=nickName;
//        this.path=path;
//        this.name=name;
//    }
//
//    public static ElasticFolderSearchInfo toElasticFolderSearchInfo(ElasticFolder elasticFolder, String nickName){
//
//        return ElasticFolderSearchInfo.builder()
//                .nickName(nickName)
//                .path(elasticFolder.getPath())
//                .name(elasticFolder.getFolderName())
//                .build();
//
//    }
//}
