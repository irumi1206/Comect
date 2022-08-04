package PoolC.Comect.repository;

import PoolC.Comect.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByEmail(String email);
    public List<User> findByUserNickname(String userNickName);
}