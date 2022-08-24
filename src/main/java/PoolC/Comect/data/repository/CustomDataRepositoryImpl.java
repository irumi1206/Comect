package PoolC.Comect.data.repository;

import PoolC.Comect.common.CustomException;
import PoolC.Comect.common.ErrorCode;
import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.domain.Link;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
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
            throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }
    }

    public void linkCreate(ObjectId rootId,String path, Link link){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        String pushQuery="";

        for(int i=0;i<tokens.length;++i) {
            if(i==0) pushQuery += "folders.$[token" + String.valueOf(i) + "]";
            else pushQuery += ".folders.$[token" + String.valueOf(i) + "]";
        }

        pushQuery+=".links";
        System.out.println(pushQuery);
        update.push(pushQuery,link);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult=mongoTemplate.updateFirst(query,update,Data.class);
        System.out.println(updateResult);

        if(updateResult.getModifiedCount()==0) {
            System.out.println(updateResult);
            throw new CustomException(ErrorCode.PATH_NOT_VALID);
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

        if(!flagFirst) throw new CustomException(ErrorCode.PATH_NOT_VALID);


        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }

        return folder.getLinks();
    }

    public List<String> folderReadFolderName(ObjectId rootId, String path){

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

        System.out.println(folder);

        if(!flagFirst) throw new CustomException(ErrorCode.PATH_NOT_VALID);

        System.out.println("sdf");

        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                System.out.println(folder.getFolders().get(j).getName());
                System.out.println(tokens[i]);
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    System.out.println("equal");
                    folder = folder.getFolders().get(j);
                    System.out.println(folder);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }

        return folder.getFolders().stream()
                .map(Folder::getName)
                .collect(Collectors.toList());
    }

    public Folder folderRead(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Data data=mongoTemplate.findOne(query,Data.class,"data");

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        if(tokens.length==0) throw new CustomException(ErrorCode.PATH_NOT_VALID);

        Folder folder=new Folder("tempFolder");

        boolean flagFirst=false;

        for(int i=0;i<data.getFolders().size();++i){
            if(data.getFolders().get(i).getName().equals(tokens[0])){
                flagFirst=true;
                folder=data.getFolders().get(i);
                break;
            }
        }

        if(!flagFirst) throw new CustomException(ErrorCode.PATH_NOT_VALID);


        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }

        return folder;
    }

    public int checkPath(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Data data=mongoTemplate.findOne(query,Data.class,"data");

        if(path.length()==0) path="/";

        String []tokens=path.split("/");
        if(tokens.length==0) return 0;

        Folder folder=new Folder("tempFolder");

        boolean flagFirst=false;

        for(int i=0;i<data.getFolders().size();++i){
            if(data.getFolders().get(i).getName().equals(tokens[0])){
                flagFirst=true;
                folder=data.getFolders().get(i);
                break;
            }
        }

        if(!flagFirst) return 1;


        for(int i=1;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) return 1;
        }

        return 0;
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
            throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }
    }

    public void folderDelete(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        if(path.length()==0) throw new CustomException(ErrorCode.PATH_NOT_VALID);

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
            throw new CustomException(ErrorCode.PATH_NOT_VALID);
        }
    }



}