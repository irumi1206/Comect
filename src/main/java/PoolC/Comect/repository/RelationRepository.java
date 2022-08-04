package PoolC.Comect.repository;

import PoolC.Comect.domain.relation.Relation;
import PoolC.Comect.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends ReactiveCrudRepository<Relation,String> {
//    List<Relation> findAllByValue(String value);
}