package PoolC.Comect.repository;

import PoolC.Comect.domain.data.Data;
import PoolC.Comect.domain.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DataRepository extends MongoRepository<Data, ObjectId> , CustomDataRepository {

}