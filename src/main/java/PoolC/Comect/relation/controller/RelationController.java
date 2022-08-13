package PoolC.Comect.relation.controller;


import PoolC.Comect.relation.dto.CreateRelationRequestDto;
import PoolC.Comect.relation.dto.FriendInfo;
import PoolC.Comect.relation.dto.ReadRelationRequestDto;
import PoolC.Comect.relation.dto.ReadRelationResponseDto;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;
    private final UserService userService;

    @PostMapping("/member/find")
    public ResponseEntity<ReadRelationResponseDto> findRelation(@RequestBody ReadRelationRequestDto request){
        try{
            User user = userService.findOne(request.getUserEmail());
            ReadRelationResponseDto readRelationResponseDto = new ReadRelationResponseDto();
            List<ObjectId> requests = relationService.findRequest(user.getRelations(), user.getId());
            List<FriendInfo> requestList = new ArrayList<>();
            for (ObjectId friendsId : requests) {
                FriendInfo friendInfo = new FriendInfo();
                User friend = userService.findById(friendsId);
                friendInfo.setId(friendsId);
                friendInfo.setNickname(friend.getUserNickname());
                friendInfo.setProfilePicture(friend.getPicture());
                requestList.add(friendInfo);
            }
            List<ObjectId> friends = relationService.findFriends(user.getRelations(), user.getId());
            List<FriendInfo> friendsList = new ArrayList<>();
            for (ObjectId friendsId : friends) {
                FriendInfo friendInfo = new FriendInfo();
                User friend = userService.findById(friendsId);
                friendInfo.setId(friendsId);
                friendInfo.setNickname(friend.getUserNickname());
                friendInfo.setProfilePicture(friend.getPicture());
                friendsList.add(friendInfo);
            }
            readRelationResponseDto.setRequest(requestList);
            readRelationResponseDto.setFriends(friendsList);
            readRelationResponseDto.setNumberOfFriends(friendsList.size());
            readRelationResponseDto.setNumberOfRequest(requestList.size());
            return ResponseEntity.ok(readRelationResponseDto);
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/member/add")
    public ResponseEntity<CreateRelationRequestDto> addRelation(@RequestBody CreateRelationRequestDto request){
        try{
            String userEmail = request.getUserEmail();
            User user = userService.findOne(userEmail);
            relationService.createRelation(user.getId(),request.getFriendId());
            return ResponseEntity.ok().build();
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
