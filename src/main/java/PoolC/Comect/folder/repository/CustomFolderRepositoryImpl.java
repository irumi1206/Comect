package PoolC.Comect.folder.repository;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.domain.Link;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
@RequiredArgsConstructor
public class CustomFolderRepositoryImpl implements CustomFolderRepository {

    private final MongoTemplate mongoTemplate;

    public boolean checkPathFolder(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Folder folder=mongoTemplate.findOne(query,Folder.class,"folder");

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if(tokens.length==0) return true;

        for(int i=0;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) return false;
        }

        return true;
    }

    public boolean checkPathLink(ObjectId rootId, String path){
        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Folder folder=mongoTemplate.findOne(query,Folder.class,"folder");

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if(tokens.length==0) return false;

        for(int i=0;i<tokens.length-1;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) return false;
        }

        for(int i=0;i<folder.getLinks().size();++i){
            if(tokens[tokens.length-1].equals(folder.getLinks().get(i).getName())) return true;
        }

        return false;
    }

    public void folderCreate(ObjectId rootId,String path, Folder folder){

        if(checkPathFolder(rootId,path+"/"+folder.getName())) throw new CustomException(ErrorCode.FILE_CONFLICT);


        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        String pushQuery="folders";

        for(int i=0;i<tokens.length;++i) {
            pushQuery += ".$[token" + String.valueOf(i) + "].folders";
        }
        update.push(pushQuery,folder);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }
        UpdateResult updateResult=mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

    public Folder folderRead(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Folder folder=mongoTemplate.findOne(query,Folder.class,"folder");

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        for(int i=0;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }

        return folder;
    }

    public void folderUpdate(ObjectId rootId, String path, String folderName){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        if(tokens.length==0) throw new CustomException(ErrorCode.PATH_NOT_VALID);

        String newPath="";
        for(int i=0;i< tokens.length-1;++i){
            newPath+=tokens[i];
            newPath+="/";
        }
        newPath+=folderName;
        if(checkPathFolder(rootId,newPath)) throw new CustomException(ErrorCode.FILE_CONFLICT);

        String pushQuery="";
        for(int i=0;i<tokens.length;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }
        pushQuery+="name";
        update.set(pushQuery,folderName);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

    public void folderDelete(ObjectId rootId, String path){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        if(tokens.length==0) throw new CustomException(ErrorCode.PATH_NOT_VALID);

        String pushQuery="";
        for(int i=0;i<tokens.length-1;++i) {
            pushQuery += "folders.$[token" + String.valueOf(i) + "].";
        }
        pushQuery+="folders";
        ;
        update.pull(pushQuery,new BasicDBObject("name",tokens[tokens.length-1]));
        for(int i=0;i< tokens.length-1;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

    public void linkCreate(ObjectId rootId,String path, Link link){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);
        String pushQuery="";

        for(int i=0;i<tokens.length;++i) {
            if(i==0) pushQuery += "folders.$[token" + String.valueOf(i) + "]";
            else pushQuery += ".folders.$[token" + String.valueOf(i) + "]";
        }
        pushQuery+=".links";
        update.push(pushQuery,link);
        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult=mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

    public Link linkRead(ObjectId rootId, String path, ObjectId id){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));

        Folder folder=mongoTemplate.findOne(query,Folder.class,"folder");

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        for(int i=0;i<tokens.length;++i){
            boolean flag=false;
            for(int j=0;j<folder.getFolders().size();++j){
                if(folder.getFolders().get(j).getName().equals(tokens[i])) {
                    folder = folder.getFolders().get(j);
                    flag=true;
                    break;
                }
            }
            if(!flag) throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }

        for(int i=0;i<folder.getLinks().size();++i){
            if(id.equals(folder.getLinks().get(i).get_id())) return folder.getLinks().get(i);
        }

        throw new CustomException(ErrorCode.PATH_NOT_FOUND);
    }

    public void linkUpdate(ObjectId rootId, String path, ObjectId id, Link link){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        String pushQuery="";

        for(int i=0;i<tokens.length;++i) {
            if(i==0)pushQuery += "folders.$[token" + String.valueOf(i) + "]";
            else pushQuery += ".folders.$[token" + String.valueOf(i) + "]";
        }
        pushQuery+=".links.$[token"+String.valueOf(tokens.length)+"]";
        update.set(pushQuery,link);

        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }
        update.filterArray(Criteria.where("token"+String.valueOf(tokens.length)+"._id").is(id));

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

    public void linkDelete(ObjectId rootId,String path, ObjectId id){

        Query query=new Query().addCriteria(Criteria.where("_id").is(rootId));
        Update update=new Update();

        String []tokens= Arrays.stream(path.split("/")).filter(e -> e.trim().length() > 0).toArray(String[]::new);

        String pushQuery="";
        for(int i=0;i<tokens.length;++i) {
            if(i==0)pushQuery += "folders.$[token" + String.valueOf(i) + "]";
            else pushQuery += ".folders.$[token" + String.valueOf(i) + "]";
        }
        pushQuery+=".links";
        update.pull(pushQuery,new BasicDBObject("_id",id));

        for(int i=0;i< tokens.length;++i){
            update.filterArray(Criteria.where("token"+String.valueOf(i)+".name").is(tokens[i]));
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Folder.class);

        if(updateResult.getModifiedCount()==0) {
            throw new CustomException(ErrorCode.PATH_NOT_FOUND);
        }
    }

}