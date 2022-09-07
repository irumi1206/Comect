package PoolC.Comect.elasticFolder.repository;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.repository.CustomElasticFolderRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomElasticFolderRepositoryImpl implements CustomElasticFolderRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticFolder> searchFolder(List<String> ids,String keyword){

        Criteria criteria=Criteria.where("ownerId").in(ids)
                .and(Criteria.where("folderName").matches(keyword));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticFolder> elasticFolderList=elasticsearchOperations.search(query,ElasticFolder.class);
        return elasticFolderList.stream().map(SearchHit::getContent).collect(Collectors.toList());

    }

    public List<ElasticFolder> searchExcludeFolder(List<String> ids,String keyword){

        Criteria criteria=Criteria.where("ownerId").notIn(ids)
                .and(Criteria.where("folderName").matches(keyword));
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticFolder> elasticFolderList=elasticsearchOperations.search(query,ElasticFolder.class);
        return elasticFolderList.stream().map(SearchHit::getContent).collect(Collectors.toList());

    }

    public void delete(String ownerId, String path){
        Criteria criteria =Criteria.where("ownerId").matches(ownerId);
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticFolder> elasticFolderList = elasticsearchOperations.search(query, ElasticFolder.class);

        for(SearchHit<ElasticFolder> current: elasticFolderList){
            ElasticFolder elasticFolder=current.getContent();
            if(!elasticFolder.getPath().startsWith(path)) continue;
            elasticsearchOperations.delete(elasticFolder);
        }
    }

    public void update(String ownerId, String path,String newName){

        System.out.println(ownerId);
        System.out.println(newName);
        Criteria criteria = Criteria.where("ownerId").matches(ownerId);
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticFolder> elasticFolderList = elasticsearchOperations.search(query, ElasticFolder.class);

        System.out.println(ownerId);
        System.out.println(newName);

        String newPath="";
        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        for(int i=0;i< tokens.length-1;++i){
            newPath+=tokens[i];
            newPath+="/";
        }
        newPath+=newName+"/";

        for(SearchHit<ElasticFolder> current: elasticFolderList){
            ElasticFolder elasticFolder=current.getContent();
            System.out.println(elasticFolder.getPath());
            if(!elasticFolder.getPath().startsWith(path)) continue;
            String originalPath=elasticFolder.getPath();
            elasticFolder.setPath(newPath+originalPath.substring(path.length()));
            if(originalPath.length()==path.length()) elasticFolder.setFolderName(newName);
            elasticsearchOperations.delete(elasticFolder.getId(),ElasticFolder.class);
            System.out.println(elasticFolder);
            elasticsearchOperations.save(elasticFolder);
        }

    }

    public void move(String ownerId, String originalPath,String destinationPath){
        Criteria criteria = Criteria.where("ownerId").matches(ownerId);
        Query query=new CriteriaQuery(criteria);
        SearchHits<ElasticFolder> elasticFolderList = elasticsearchOperations.search(query, ElasticFolder.class);
        String []tokens= Arrays.stream(originalPath.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        String currentFolderName=tokens[tokens.length-1]+"/";

        for(SearchHit<ElasticFolder> current: elasticFolderList){
            ElasticFolder elasticFolder=current.getContent();
            if(!elasticFolder.getPath().startsWith(originalPath)) continue;
            String currentPath=elasticFolder.getPath();
            elasticFolder.setPath(destinationPath+currentFolderName+currentPath.substring(originalPath.length()));
            elasticsearchOperations.save(elasticFolder);
        }

    }
}
