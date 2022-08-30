package PoolC.Comect.folder.dto;

import PoolC.Comect.folder.domain.Link;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkInfo {

    private String id;
    private String name;
    private String imageUrl;
    private String url;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkInfo(String id, String name, String imageUrl, String url, List<String> keywords,String isPublic){
        this.id=id;
        this.name=name;
        this.imageUrl=imageUrl;
        this.url=url;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }

    public static List<LinkInfo> toLinkInfo(List<Link> links) {

        List<LinkInfo> linkInfoList = new ArrayList<>();

        for (Link currentLink : links) {
            linkInfoList.add(LinkInfo.builder()
                    .id(currentLink.get_id().toString())
                    .name(currentLink.getName())
                    .imageUrl(currentLink.getImageUrl())
                    .url(currentLink.getUrl())
                    .keywords(currentLink.getKeywords())
                    .isPublic(currentLink.getIsPublic())
                    .build());

        }

        return linkInfoList;
    }
}
