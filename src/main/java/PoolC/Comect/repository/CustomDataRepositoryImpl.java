package PoolC.Comect.repository;

import PoolC.Comect.domain.data.Data;
import PoolC.Comect.domain.data.Folder;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomDataRepositoryImpl implements CustomDataRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertToFolder(ObjectId id, Folder folder){
        UpdateResult result=mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(id)),
                new Update().push("folders",folder),
                Data.class
        );
    }


}
