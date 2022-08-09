package PoolC.Comect.repository;

import PoolC.Comect.domain.relation.Relation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends MongoRepository<Relation, ObjectId> {

}