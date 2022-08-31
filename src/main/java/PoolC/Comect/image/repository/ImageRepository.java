package PoolC.Comect.image.repository;

import PoolC.Comect.image.domain.Image;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, ObjectId> {
    @Override
    public Optional<Image> findById(ObjectId objectId);
}
