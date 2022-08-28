package PoolC.Comect.relation.controller;


import PoolC.Comect.relation.domain.FriendInfo;
import PoolC.Comect.relation.dto.*;
import PoolC.Comect.relation.service.RelationService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RelationController {

    private final RelationService relationService;
    private final UserService userService;


    @GetMapping("/friend")
    public ResponseEntity<ReadRelationResponseDto> findRelation(@ModelAttribute ReadRelationRequestDto request){
        log.info("Called GET/friend, \tparameter: "+request.toString());
        List<ObjectId> requestList = relationService.findRequestIds(request.getEmail());
        List<ObjectId> friendsList = relationService.findFriendIds(request.getEmail());
        List<FriendInfo> requestFriendsList = relationService.listToInfo(requestList);
        List<FriendInfo> acceptedFriendsList = relationService.listToInfo(friendsList);
        ReadRelationResponseDto readRelationResponseDto = new ReadRelationResponseDto();
        readRelationResponseDto.setNumberOfFriend(friendsList.size());
        readRelationResponseDto.setNumberOfRequest(requestList.size());
        readRelationResponseDto.setRequest(requestFriendsList);
        readRelationResponseDto.setFriends(acceptedFriendsList);
        return ResponseEntity.ok(readRelationResponseDto);
    }

    @PostMapping("/friend")
    public ResponseEntity<CreateRelationRequestDto> addRelation(@RequestBody CreateRelationRequestDto request){
        log.info("Called POST/friend, \tbody: "+request.toString());
        relationService.createRelation(request.getEmail(), request.getFriendNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend/request")
    public ResponseEntity<Void> acceptRelation(@RequestBody AcceptRelationRequestDto request){
        log.info("Called POST/friend/request, \tbody: "+request.toString());
        relationService.acceptRelation(request.getEmail(), request.getFriendNickname());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friend/request")
    public ResponseEntity<Void> rejectRelation(@RequestBody RejectRelationRequestDto request){
        log.info("Called DELETE/friend/request, \tbody: "+request.toString());
        relationService.rejectRelation(request.getEmail(), request.getFriendNickname());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friend")
    public ResponseEntity<Void> deleteRelation(@RequestBody DeleteFriendRequestDto request){
        log.info("Called DELETE/friend, \tbody: "+request.toString());
        relationService.deleteRelation(request.getEmail(), request.getFriendNickname());
        return ResponseEntity.ok().build();
    }
}

