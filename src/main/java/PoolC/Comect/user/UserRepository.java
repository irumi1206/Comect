package PoolC.Comect.user;

import PoolC.Comect.domain.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    @Override
    public Optional<User> findById(ObjectId objectId);

    public Optional<User> findByEmail(String email);

    public List<User> findByUserNickname(String userNickName);


}