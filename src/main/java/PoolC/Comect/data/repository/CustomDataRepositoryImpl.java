package PoolC.Comect.data.repository;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.domain.Folder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomDataRepositoryImpl implements CustomDataRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createFolder(ObjectId rootId,String path, Folder folder){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Update update=new Update();

        String []tokens=path.split("/");
        String pushQuery="folders";
        for(int i=0;i<tokens.length;++i) {
            pushQuery += ".$[token" + String.valueOf(i) + "].folders";
        }

        update.push(pushQuery,folder);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        mongoTemplate.updateFirst(query,update,Data.class);
    }

    public List<Folder> readFolder(ObjectId rootId, String path){
        return null;
    }

    public void updateFolder(ObjectId rootId,String path,String key,String value){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Update update=new Update();

        String []tokens=path.split("/");
        String pushQuery="";
        for(int i=0;i<tokens.length;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }
        pushQuery+=key;

        System.out.println(pushQuery);

        update.set(pushQuery,value);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
            System.out.println("token"+String.valueOf(i)+".name  "+tokens[i]);
        }

        mongoTemplate.updateFirst(query,update,Data.class);
    }

    public void deleteFolder(ObjectId rootId,String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Update update=new Update();

        String []tokens=path.split("/");
        String pushQuery="";
        for(int i=0;i<tokens.length-1;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }

        pushQuery+="folders";

        System.out.println(pushQuery);
        System.out.println(tokens[tokens.length-1]);

        update.pull(pushQuery,new Folder("name",tokens[tokens.length-1]));
        for(int i=0;i< tokens.length-1;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
            System.out.println("token"+String.valueOf(i)+".name  "+tokens[i]);
        }

        mongoTemplate.updateFirst(query,update,Data.class);
    }

    @Override
    public void createLink(ObjectId id, String path, Folder folder) {

    }

    @Override
    public void deleteLink(ObjectId id, String path) {

    }

    @Override
    public void updateLink(ObjectId id, String path, String name, String newName) {

    }


}