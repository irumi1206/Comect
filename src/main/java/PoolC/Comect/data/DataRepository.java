package PoolC.Comect.data;

import PoolC.Comect.domain.data.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DataRepository extends MongoRepository<Data, ObjectId> , CustomDataRepository {

}