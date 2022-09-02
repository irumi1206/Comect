package PoolC.Comect.elasticFolder.repository;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomElasticLinkRepositoryImpl implements  CustomElasticLinkRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticLink> searchLinkMe(String id, String keyword){
        System.out.println(id);
        System.out.println(keyword);
        Criteria criteria=Criteria.where("ownerId").matches(id);
                //.and(Criteria.where("linkName").matches(keyword));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits=elasticsearchOperations.search(query,ElasticLink.class);
        return elasticLinkSearchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public List<ElasticLink> searchLink(List<String> ids, String keyword){

        Criteria criteria=Criteria.where("ownerId").in(ids)
                .and(Criteria.where("linkName").matches(keyword))
                .and(Criteria.where("isPublic").matches("true"));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits=elasticsearchOperations.search(query,ElasticLink.class);
        return elasticLinkSearchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

    }

    public List<ElasticLink> searchExcludeLink(List<String> ids,String keyword){

        Criteria criteria=Criteria.where("ownerId").notIn(ids)
                .and(Criteria.where("linkName").matches(keyword))
                .and(Criteria.where("isPublic").matches("true"));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits=elasticsearchOperations.search(query,ElasticLink.class);
        return elasticLinkSearchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

    }

    public void delete(String ownerId, String path){}
    public void update(String ownerId, String path,String newName){}
    public void move(String ownerId, String originalPath, String destinationPath){}

    public void deleteFolder(String ownerId, String path){}
    public void updateFolder(String ownerId, String path,String newName){}
    public void moveFolder(String ownerId, String originalPath, String destinationPath){}
}
