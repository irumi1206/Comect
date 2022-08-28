package PoolC.Comect.relation.service;

import PoolC.Comect.exception.CustomException;
import PoolC.Comect.exception.ErrorCode;
import PoolC.Comect.relation.domain.FriendInfo;
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
    public void createRelation(String email, String friendNickname){
        User me = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        User friend = userRepository.findByNickname(friendNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND) );
        List<ObjectId> relations = me.getRelations();
        for (ObjectId relationId : relations) {
            relationRepository.findById(relationId).ifPresent((relation)->{
                if(relation.getSenderId().equals(friend.getId())||relation.getReceiverId().equals(friend.getId())) {
                    throw new CustomException(ErrorCode.REQUEST_EXIST);
                }
            });
        }
        Relation relation = new Relation(me.getId(), friend.getId());
        relationRepository.save(relation);
        me.getRelations().add(relation.getId());
        friend.getRelations().add(relation.getId());
        userRepository.save(me);
        userRepository.save(friend);
    }

    //request 찾기, 내가 받은 request만 뜸
    public Relation findRequest(String email, String friendNickname){
        User me = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        User friend = userRepository.findByNickname(friendNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND) );

        for(ObjectId relationId:me.getRelations()){
            Optional<Relation> relationOption = relationRepository.findById(relationId);
            if(!relationOption.isEmpty()){
                Relation relation = relationOption.get();
                if(relation.getReceiverId().equals(me.getId())&&relation.getSenderId().equals(friend.getId())){
                    return relation;
                }
            }
        }
        throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
    }

    //relation 찾기, 내가 보낸 것과 받은것 모두 포함
    public Relation findRelation(ObjectId myId, ObjectId friendId){
        User me = userRepository.findById(myId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        User friend = userRepository.findById(friendId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        for(ObjectId relationId:me.getRelations()){
            Optional<Relation> relationOption = relationRepository.findById(relationId);
            if(!relationOption.isEmpty()){
                Relation relation = relationOption.get();
                if(relation.getReceiverId().equals(me.getId())&&relation.getSenderId().equals(friend.getId())){
                    return relation;
                }else if(relation.getSenderId().equals(me.getId())&&relation.getReceiverId().equals(friend.getId())){
                    return relation;
                }
            }
        }
        throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
    }

    @Transactional
    public void acceptRelation(String email,String friendNickname){
        Relation relation = findRequest(email,friendNickname);
        if(relation.getRelationType().equals(RelationType.REQUEST)){
            relation.setRelationType(RelationType.BOTH);
            relationRepository.save(relation);
        }else{
            throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
        }
    }

    @Transactional
    public void rejectRelation(String email,String friendNickname){
        Relation relation = findRequest(email,friendNickname);
        if(relation.getRelationType().equals(RelationType.REQUEST)){
            relation.setRelationType(RelationType.REJECTED);
            relationRepository.save(relation);
        }else{
            throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteRelation(String email, String friendNickname){
        User me = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        User friend = userRepository.findByNickname(friendNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND) );

        Relation relation = findRelation(me.getId(),friend.getId());
        if(!relation.getRelationType().equals(RelationType.DELETED)) {
            relation.setRelationType(RelationType.DELETED);
            userRepository.save(me);
            userRepository.save(friend);
            relationRepository.save(relation);
        }else{
            throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
        }
    }

    public List<ObjectId> findFriendIds(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<ObjectId> relations = user.getRelations();
        List<ObjectId> friendId = new ArrayList<>();
        for (ObjectId relationId : relations) {
            relationRepository.findById(relationId).ifPresent((relation)->{
                if(relation.getRelationType().equals(RelationType.BOTH)){
                    if(relation.getSenderId().equals(user.getId())){
                        friendId.add(relation.getReceiverId());
                    }else{
                        friendId.add(relation.getSenderId());
                    }
                }
            });
        }
        return friendId;
    }

    public List<FriendInfo> listToInfo(List<ObjectId> ids){
        List<FriendInfo> friendInfos=new ArrayList<>();
        for(ObjectId friendId:ids){
            userRepository.findById(friendId).ifPresent((user)->{
                FriendInfo friendInfo = new FriendInfo(user.getEmail(),user.getNickname(),user.getImageUrl());
                friendInfos.add(friendInfo);
            });
        }
        return friendInfos;
    }

    public List<ObjectId> findRequestIds(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<ObjectId> relations = user.getRelations();
        List<ObjectId> requestId = new ArrayList<>();
        for (ObjectId relationId : relations) {
            relationRepository.findById(relationId).ifPresent((relation)->{
                if(relation.getRelationType().equals(RelationType.REQUEST)&&relation.getReceiverId().equals(user.getId())){
                    requestId.add(relation.getSenderId());
                }
            });
        }
        return requestId;
    }


}
