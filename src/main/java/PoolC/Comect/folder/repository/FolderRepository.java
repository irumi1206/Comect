package PoolC.Comect.folder.repository;

import PoolC.Comect.folder.domain.Folder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FolderRepository extends MongoRepository<Folder, ObjectId> , CustomFolderRepository {

}