package PoolC.Comect.relation.controller;


import PoolC.Comect.relation.dto.*;
import PoolC.Comect.relation.service.RelationService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.CreateUserRequestDto;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;
    private final UserService userService;

    @PostMapping("/member/find")
    public ResponseEntity<ReadRelationResponseDto> findRelation(@RequestBody ReadRelationRequestDto request){
            User user = userService.findOne(request.getUserEmail());
            ReadRelationResponseDto readRelationResponseDto = new ReadRelationResponseDto();
            List<ObjectId> requests = relationService.findRequest(user.getRelations(), user.getId());
            List<FriendInfo> requestList = new ArrayList<>();
            for (ObjectId friendsId : requests) {
                FriendInfo friendInfo = new FriendInfo();
                User friend = userService.findById(friendsId);
                friendInfo.setId(friendsId.toHexString());
                friendInfo.setNickname(friend.getUserNickname());
                friendInfo.setProfilePicture(friend.getPicture());
                requestList.add(friendInfo);
            }
            List<ObjectId> friends = relationService.findFriends(user.getRelations(), user.getId());
            List<FriendInfo> friendsList = new ArrayList<>();
            for (ObjectId friendsId : friends) {
                FriendInfo friendInfo = new FriendInfo();
                User friend = userService.findById(friendsId);
                friendInfo.setId(friendsId.toHexString());
                friendInfo.setNickname(friend.getUserNickname());
                friendInfo.setProfilePicture(friend.getPicture());
                friendsList.add(friendInfo);
            }
            readRelationResponseDto.setRequest(requestList);
            readRelationResponseDto.setFriends(friendsList);
            readRelationResponseDto.setNumberOfFriends(friendsList.size());
            readRelationResponseDto.setNumberOfRequest(requestList.size());
            return ResponseEntity.ok(readRelationResponseDto);
    }

    @PostMapping("/member/add")
    public ResponseEntity<CreateRelationRequestDto> addRelation(@RequestBody CreateRelationRequestDto request){
            String userEmail = request.getUserEmail();
            String friendEmail = request.getFriendEmail();
            User user = userService.findOne(userEmail);
            User friend = userService.findOne(friendEmail);
            relationService.createRelation(user.getId(),friend.getId());
            return ResponseEntity.ok().build();
    }

    @PostMapping("/member/accept")
    public ResponseEntity<Void> acceptRelation(@RequestBody AcceptRelationRequestDto request){
            String userEmail = request.getUserEmail();
            String friendId = request.getFriendId();
            User user = userService.findOne(userEmail);
            relationService.acceptRelation(user.getId(),new ObjectId(friendId));
            return ResponseEntity.ok().build();
    }

    @PostMapping("/member/reject")
    public ResponseEntity<Void> rejectRelation(@RequestBody RejectRelationRequestDto request){
        String userEmail = request.getUserEmail();
        String friendId = request.getFriendId();
        User user = userService.findOne(userEmail);
        relationService.rejectRelation(user.getId(),new ObjectId(friendId));
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/member/delete")
//    public ResponseEntity<Void> deleteRelation(@RequestBody DeleteFriendRequestDto request){
//        String userEmail = request.getUserEmail();
//        String friendEmail = request.getFriendEmail();
//        relationService.deleteRelation(userEmail, friendEmail);
//        return ResponseEntity.ok().build();
//    }
}
