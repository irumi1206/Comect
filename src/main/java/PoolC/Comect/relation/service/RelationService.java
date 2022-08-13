package PoolC.Comect.relation.service;

import PoolC.Comect.relation.domain.Relation;
import PoolC.Comect.relation.domain.RelationType;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.relation.repository.RelationRepository;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationService {

    private final UserRepository userRepository;
    private final RelationRepository relationRepository;

    @Transactional
    public void createRelation(ObjectId id1, ObjectId id2){
        User user1 = userRepository.findById(id1).get();
        User user2 = userRepository.findById(id2).get();
        Relation relation = new Relation(id1, id2);
        user1.getRelations().add(relation.getId());
        user2.getRelations().add(relation.getId());
        relationRepository.save(relation);
    }

    @Transactional
    public void changeState(ObjectId id, RelationType relationType){
        Relation relation = relationRepository.findById(id).get();
        relation.setRelationType(relationType);
    }
}