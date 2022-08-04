package PoolC.Comect.repository;

import PoolC.Comect.domain.data.Data;
import PoolC.Comect.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends ReactiveCrudRepository<Data,String> {
//    List<Data> findAllByValue(String value);
}