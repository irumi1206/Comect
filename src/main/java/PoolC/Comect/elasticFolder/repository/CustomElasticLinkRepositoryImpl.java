package PoolC.Comect.elasticFolder.repository;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomElasticLinkRepositoryImpl implements  CustomElasticLinkRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticLink> searchLinkMe(String id, String keyword){
        Criteria criteria=Criteria.where("ownerId").matches(id)
                .and(Criteria.where("linkName").matches(keyword));
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

    public void delete(String ownerId, String path, String id){
        System.out.println(ownerId);
        System.out.println(path);
        System.out.println(id);
        Criteria criteria = Criteria.where("path").matches(path)
                .and(Criteria.where("ownerId").matches(ownerId))
                .and(Criteria.where("linkId").matches(id));
        Query query=new CriteriaQuery(criteria);
        ByQueryResponse a =elasticsearchOperations.delete(query, ElasticLink.class);
        System.out.println(a.getDeleted());
    }

    public void update(String ownerId, String path, String id, String newId, String newName){
        Criteria criteria = Criteria.where("path").matches(path)
                .and(Criteria.where("ownerId").matches(ownerId))
                .and(Criteria.where("linkId").matches(id));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits = elasticsearchOperations.search(query, ElasticLink.class);

        for(SearchHit<ElasticLink> elasticLinkSearchHit:elasticLinkSearchHits){
            ElasticLink elasticLink=elasticLinkSearchHit.getContent();
            elasticLink.setLinkName(newName);
            elasticLink.setLinkId(newId);
            elasticsearchOperations.save(elasticLink);
        }
    }

    public void move(String ownerId, String originalPath, String id, String destinationPath){

        Criteria criteria = Criteria.where("path").matches(originalPath)
                .and(Criteria.where("ownerId").matches(ownerId))
                .and(Criteria.where("linkId").matches(id));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits = elasticsearchOperations.search(query, ElasticLink.class);

        for(SearchHit<ElasticLink> current: elasticLinkSearchHits){
            ElasticLink elasticLink=current.getContent();
            String currentPath=elasticLink.getPath();
            elasticLink.setPath(destinationPath+currentPath.substring(originalPath.length()));
            elasticsearchOperations.save(elasticLink);
        }
    }

    public void deleteFolder(String ownerId, String path){
        System.out.println(ownerId);
        System.out.println(path);
        Criteria criteria = Criteria.where("path").startsWith(path)
                .and(Criteria.where("ownerId").matches(ownerId));
        Query query=new CriteriaQuery(criteria);
        elasticsearchOperations.delete(query, ElasticLink.class);
    }
    public void updateFolder(String ownerId, String path,String newName){

        Criteria criteria = Criteria.where("path").startsWith(path)
                .and(Criteria.where("ownerId").matches(ownerId));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits = elasticsearchOperations.search(query, ElasticLink.class);

        String newPath="";
        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        for(int i=0;i< tokens.length-1;++i){
            newPath+=tokens[i];
            newPath+="/";
        }
        newPath+=newName+"/";

        for(SearchHit<ElasticLink> current: elasticLinkSearchHits){
            ElasticLink elasticLink=current.getContent();
            String originalPath=elasticLink.getPath();
            elasticLink.setPath(newPath+originalPath.substring(path.length()));
            System.out.println(elasticLink);
            elasticsearchOperations.save(elasticLink);
        }
    }
    public void moveFolder(String ownerId, String originalPath, String destinationPath){

        Criteria criteria = Criteria.where("path").startsWith(originalPath)
                .and(Criteria.where("ownerId").matches(ownerId));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticLink> elasticLinkSearchHits = elasticsearchOperations.search(query, ElasticLink.class);
        String []tokens= Arrays.stream(originalPath.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        String currentFolderName=tokens[tokens.length-1]+"/";

        for(SearchHit<ElasticLink> current: elasticLinkSearchHits){
            ElasticLink elasticLink=current.getContent();
            String currentPath=elasticLink.getPath();
            elasticLink.setPath(destinationPath+currentFolderName+currentPath.substring(originalPath.length()));
            elasticsearchOperations.save(elasticLink);
        }
    }
}
