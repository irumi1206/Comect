package PoolC.Comect.data.repository;

import PoolC.Comect.data.domain.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DataRepository extends MongoRepository<Data, ObjectId> , CustomDataRepository {

}