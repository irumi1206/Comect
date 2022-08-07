package PoolC.Comect.repository;

import PoolC.Comect.domain.data.Folder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomDataRepository {

    void insertToFolder(ObjectId id, Folder folder);
}
