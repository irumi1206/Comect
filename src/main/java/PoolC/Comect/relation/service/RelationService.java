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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<ObjectId> relations = user1.getRelations();
        for (ObjectId relation : relations) {
            Optional<Relation> relationOption = relationRepository.findById(relation);
            if(!relationOption.isEmpty()){
                if(relationOption.get().getRelationId1().equals(id2)){
                    throw new IllegalStateException("이미 존재하는 요청입니다.");
                }
                else if(relationOption.get().getRelationId2().equals(id2)){
                    throw new IllegalStateException("이미 존재하는 요청입니다.");
                }
            }
        }
        Relation relation = new Relation(id1, id2);
        user1.getRelations().add(relation.getId());
        user2.getRelations().add(relation.getId());
        relationRepository.save(relation);
    }

    @Transactional
    public void acceptRelation(ObjectId id,ObjectId myId, RelationType relationType){
        Optional<Relation> relationOption = relationRepository.findById(id);
        if(relationOption.isEmpty()){
            throw new IllegalStateException("해당 요청이 존재하지 않습니다.");
        }
        Relation relation = relationOption.get();
        if(myId.equals(relation.getRelationId2())){
            relation.setRelationType(relationType);
        }else{
            throw new IllegalStateException("요청을 처리할 권한이 없습니다.");
        }
    }

    @Transactional
    public void cancelRelation(ObjectId id, ObjectId myId){
        Optional<Relation> relationOption = relationRepository.findById(id);
        if(relationOption.isEmpty()){
            throw new IllegalStateException("해당 요청이 존재하지 않습니다.");
        }
        Relation relation = relationOption.get();
        if(myId.equals(relation.getRelationId1())){
            relation.setRelationType(RelationType.REJECTED);
        }else{
            throw new IllegalStateException("요청을 처리할 권한이 없습니다.");
        }
    }

    public List<ObjectId> findFriends(List<ObjectId> relations,ObjectId myId){
        List<ObjectId> friendId = new ArrayList<>();
        for (ObjectId relationId : relations) {
            Optional<Relation> relationOption = relationRepository.findById(relationId);
            if(!relationOption.isEmpty()){
                Relation relation = relationOption.get();
                if(relation.getRelationType().equals(RelationType.BOTH)){
                    if(relation.getRelationId2().equals(myId)){
                        friendId.add(relation.getRelationId1());
                    }else{
                        friendId.add(relation.getRelationId2());
                    }
                }
            }
        }
        return friendId;
    }
    public List<ObjectId> findRequest(List<ObjectId> relations,ObjectId myId){
        List<ObjectId> friendId = new ArrayList<>();
        for (ObjectId relationId : relations) {
            Optional<Relation> relationOption = relationRepository.findById(relationId);
            if(!relationOption.isEmpty()){
                Relation relation = relationOption.get();
                if(relation.getRelationType().equals(RelationType.REQUEST)){
                    if(relation.getRelationId2().equals(myId)){
                        friendId.add(relation.getRelationId1());
                    }else{
                        friendId.add(relation.getRelationId2());
                    }
                }
            }
        }
        return friendId;
    }
}
