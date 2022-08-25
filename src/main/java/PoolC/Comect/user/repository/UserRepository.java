package PoolC.Comect.user.repository;

import PoolC.Comect.user.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    @Override
    public Optional<User> findById(ObjectId objectId);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByNickname(String nickname);
}