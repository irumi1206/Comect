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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationService {

    private final UserRepository userRepository;
    private final RelationRepository relationRepository;

    @Transactional
    public void createRelation(ObjectId id1, ObjectId id2){
        Optional<User> user1Option = userRepository.findById(id1);
        Optional<User> user2Option = userRepository.findById(id2);
        if(user1Option.isEmpty()){
            throw new NullPointerException("해당 아이디의 유저가 존재하지 않습니다.");
        }
        if(user2Option.isEmpty()){
            throw new NullPointerException("해당 아이디의 유저가 존재하지 않습니다.");
        }
        User user1 = user1Option.get();
        User user2 = user2Option.get();
        List<ObjectId> relations = user1.getRelations();
        System.out.println(relations.size());
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
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Transactional
    public void acceptRelation(ObjectId relationId,ObjectId myId){
        Optional<Relation> relationOption = relationRepository.findById(relationId);
        if(relationOption.isEmpty()){
            throw new NullPointerException("해당 요청이 존재하지 않습니다.");
        }
        Relation relation = relationOption.get();
        if(!relation.getRelationType().equals(RelationType.REQUEST)){
            throw new IllegalStateException("이미 친구이거나 거절되었습니다.");
        }
        if(myId.equals(relation.getRelationId2())){
            relation.setRelationType(RelationType.BOTH);
            relationRepository.save(relation);
        }else{
            throw new IllegalStateException("요청을 처리할 권한이 없습니다.");
        }
    }

    @Transactional
    public void rejectRelation(ObjectId id, ObjectId myId){
        Optional<Relation> relationOption = relationRepository.findById(id);
        if(relationOption.isEmpty()){
            throw new NullPointerException("해당 요청이 존재하지 않습니다.");
        }
        Relation relation = relationOption.get();
        if(myId.equals(relation.getRelationId2())){
            relation.setRelationType(RelationType.REJECTED);
            relationRepository.save(relation);
        }else{
            throw new IllegalStateException("요청을 처리할 권한이 없습니다.");
        }
    }

//    @Transactional
//    public void deleteRelation(String userEmail, String friendEmail){
//        User user = userRepository.findByEmail(userEmail).get();
//        User friend = userRepository.findByEmail(friendEmail).get();
//        ObjectId userId2 = friend.getId();
//        List<ObjectId> relations = user.getRelations();
//        ObjectId id;
//        for (ObjectId relationId : relations) {
//            Relation relation = relationRepository.findById(relationId).get();
//            if(relation.getRelationId1().equals(userId2) ||relation.getRelationId2().equals(userId2)){
//                id=relation.getId();
//                break;
//            }
//        }
//        Optional<Relation> relationOption = relationRepository.findById(id);
//        if(relationOption.isEmpty()){
//            throw new IllegalStateException("해당 요청이 존재하지 않습니다.");
//        }
//        Relation relation = relationOption.get();
//        ObjectId user1Id = relation.getRelationId1();
//        ObjectId user2Id = relation.getRelationId2();
//        userRepository.findById(user1Id).get().getRelations().remove(relation.getId());
//        userRepository.findById(user2Id).get().getRelations().remove(relation.getId());
//        relationRepository.delete(relation);
//    }

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
        if(relations.size()==0){
            return friendId;
        }
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
