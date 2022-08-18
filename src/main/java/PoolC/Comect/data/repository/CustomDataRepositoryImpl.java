package PoolC.Comect.data.repository;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.domain.Link;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomDataRepositoryImpl implements CustomDataRepository {

    private final MongoTemplate mongoTemplate;

    public void folderCreate(ObjectId rootId,String path, Folder folder){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        String pushQuery="folders";

        for(int i=0;i<tokens.length;++i) {
            pushQuery += ".$[token" + String.valueOf(i) + "].folders";
        }
        update.push(pushQuery,folder);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult=mongoTemplate.updateFirst(query,update,Data.class);

        if(updateResult.getModifiedCount()==0) {
            throw new IllegalStateException("경로가 유효하지 않음");
        }
    }

    public List<Link> folderReadLink(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Data data=mongoTemplate.findOne(query,Data.class,"data");

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        if(tokens.length==0) {
            return data.getLinks();
        }

        Folder folder=new Folder("tempFolder");

        boolean flagFirst=false;

        for(int i=0;i<data.getFolders().size();++i){
            if(data.getFolders().get(i).getName().equals(tokens[0])){
                flagFirst=true;
                folder=data.getFolders().get(i);
                break;
            }
        }

        if(!flagFirst) throw new IllegalStateException("경로가 유효하지 않습니다");


        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(i).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(i);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new IllegalStateException("경로가 유효하지 않습니다");
        }

        return folder.getLinks();
    }

    public List<String> folderReadFolder(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Data data=mongoTemplate.findOne(query,Data.class,"data");

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        if(tokens.length==0) {
            return data.getFolders().stream()
                    .map(Folder::getName)
                    .collect(Collectors.toList());
        }

        Folder folder=new Folder("tempFolder");

        boolean flagFirst=false;

        for(int i=0;i<data.getFolders().size();++i){
            if(data.getFolders().get(i).getName().equals(tokens[0])){
                flagFirst=true;
                folder=data.getFolders().get(i);
                break;
            }
        }

        if(!flagFirst) throw new IllegalStateException("경로가 유효하지 않습니다");


        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(i).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(i);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new IllegalStateException("경로가 유효하지 않습니다");
        }

        return folder.getFolders().stream()
                .map(Folder::getName)
                .collect(Collectors.toList());
    }

    public void folderUpdate(ObjectId rootId, String path, String folderName){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        String pushQuery="";
        for(int i=0;i<tokens.length;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }
        pushQuery+="name";
        update.set(pushQuery,folderName);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Data.class);

        if(updateResult.getModifiedCount()==0) {
            throw new IllegalStateException("경로가 유효하지 않음");
        }
    }

    public void folderDelete(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        if(path.length()==0) throw new IllegalStateException("루트폴더를 삭제할수 없습니다");

        String []tokens=path.split("/");
        String pushQuery="";
        for(int i=0;i<tokens.length-1;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }
        pushQuery+="folders";
        ;
        System.out.println(pushQuery);
        update.pull(pushQuery,new BasicDBObject("name",tokens[tokens.length-1]));
        for(int i=0;i< tokens.length-1;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Data.class);

        if(updateResult.getModifiedCount()==0) {
            throw new IllegalStateException("경로가 유효하지 않음");
        }
    }



}