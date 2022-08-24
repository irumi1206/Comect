package PoolC.Comect.data.dto;

import PoolC.Comect.data.domain.Link;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LinkInfo {

    private String id;
    private String title;
    private String imgUrl;
    private String url;
    private List<String> keywords;
    private String isPublic;

    @Builder
    public LinkInfo(String id, String title, String imgUrl, String url, List<String> keywords,String isPublic){
        this.id=id;
        this.title=title;
        this.imgUrl=imgUrl;
        this.url=url;
        this.keywords=keywords;
        this.isPublic=isPublic;
    }

    public static List<LinkInfo> toLinkInfo(List<Link> links) {

        List<LinkInfo> temp = new ArrayList<>();

        for (int i = 0; i < links.size(); ++i) {
            temp.add(LinkInfo.builder()
                    .id(links.get(i).getId().toString())
                    .title(links.get(i).getTitle())
                    .imgUrl(links.get(i).getImage())
                    .url(links.get(i).getUrl())
                    .keywords(links.get(i).getKeywords())
                    .isPublic(links.get(i).getIsPublic())
                    .build());

        }

        return temp;
    }
}
