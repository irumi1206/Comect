package PoolC.Comect.user.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.FollowInfo;
import PoolC.Comect.user.domain.ImageUploadData;
import PoolC.Comect.user.domain.MemberData;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import com.mongodb.client.ClientSession;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    //회원 가입
    @Transactional
    public boolean join(String email, String password, String nickname, MultipartFile multipartFile){
        //validation check
        validateEmailUser(email);
        validateDuplicateUser(email);
        validateDuplicateNickname(nickname);
        //validatePassword(password);

        //이미지 저장
        ImageUploadData imageUploadData = imageService.createImage(multipartFile, email);
        Folder folder=new Folder("");
        folderRepository.save(folder);
        User user=new User(nickname,email,folder.get_id(),imageUploadData.getImageUrl(), password);
        userRepository.save(user);
        return imageUploadData.isSuccess();
    }



    public void login(String email, String password){
        //validateEmailUser(email);
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.LOGIN_FAIL));
        if(!user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    public boolean update(String email,String userNickname, MultipartFile newMultipartFile, String imageChange){
        //validateEmailUser(email);
        User user = findOneEmail(email);
        if(!user.getNickname().equals(userNickname)){
            validateDuplicateNickname(userNickname);
            user.setNickname(userNickname);
        }
        boolean changeSuccess;
        if(imageChange.equals("true")){
            ObjectId postImageId=user.getImageId();
            ImageUploadData imageUploadData = imageService.createImage(newMultipartFile, email);
            changeSuccess = imageUploadData.isSuccess();
            if(changeSuccess){
                imageService.deleteImage(postImageId);
                user.setImageUrl(imageUploadData.getImageUrl());
            }
        }else{
            changeSuccess=true;
        }
        else changeSuccess=true;

        userRepository.save(user);
        return changeSuccess;
    }

    public User findOneEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public User findOneNickname(String nickname){
        return userRepository.findByNickname(nickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public User findOneId(ObjectId id){
        return userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    //@Transactional
    public void createFollow(String email, String followedNickname){
        User user = findOneEmail(email);
        User followed = userRepository.findByNickname(followedNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(user.getFollowings().contains(followed.getId())){
            throw new CustomException(ErrorCode.FOLLOWING_EXIST);
        }
        user.getFollowings().add(followed.getId());
        followed.getFollowers().add(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
    }

    public MemberData getMemberInfo(String nickname){
        User user = userRepository.findByNickname(nickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        MemberData memberData = MemberData.builder()
                .follower(user.getFollowers().size())
                .following(user.getFollowings().size())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .build();
        return memberData;
    }

    //user가 followed를 팔로우 중인지 확인하는 함수
    public boolean isFollower(ObjectId id, ObjectId followedId){
        User user = userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        User followed = userRepository.findById(followedId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        for (ObjectId followerId : user.getFollowers()) {
            if(followed.getId().equals(followerId)){
                return true;
            }
        }
        return false;
    }
    public List<ObjectId> readFollowingById(ObjectId id) {
        User user = findOneId(id);
        List<ObjectId> followings = user.getFollowings();
        return followings;
    }

    public List<ObjectId> readFollowedById(ObjectId id) {
        User user = findOneId(id);
        List<ObjectId> followeds = user.getFollowers();
        return followeds;
    }

    public void validateDuplicateUser(String email){
        if(!userRepository.findByEmail(email).isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    //나중에 닉네임 중복 조회 서비스에서 사용
    private void validateDuplicateNickname(String nickname){
        if(!userRepository.findByNickname(nickname).isEmpty()){
            throw new CustomException(ErrorCode.NICKNAME_EXISTS);
        }
    }

    public List<FollowInfo> readFollower(String email) {
        User user = findOneEmail(email);
        List<ObjectId> followers = user.getFollowers();
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followerInfos=listToInfo(followers,followings);
        return followerInfos;
    }

    public List<FollowInfo> readFollowing(String email) {
        User user = findOneEmail(email);
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followingInfos=listToInfo(followings, followings);
        return followingInfos;
    }

    //@Transactional
    public void deleteFollow(String email, String followedNickname) {
        User user = findOneEmail(email);
        User followed = userRepository.findByNickname(followedNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        user.getFollowings().remove(followed.getId());
        followed.getFollowers().remove(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
    }

    private List<FollowInfo> listToInfo(List<ObjectId> list, List<ObjectId> followings){
        List<FollowInfo> followInfos=new ArrayList<>();
        for (ObjectId followId : list) {
            userRepository.findById(followId).ifPresent((following)->{
                FollowInfo followInfo = FollowInfo.builder()
                        .email(following.getEmail())
                        .nickname(following.getNickname())
                        .imageUrl(following.getImageUrl())
                        .isFollowing(followings.contains(followId))
                        .build();
                followInfos.add(followInfo);
            });
        }
        return followInfos;
    }

    public void deleteMember(String email, String password) {
        User user = findOneEmail(email);
        if(!user.getPassword().equals(password)){
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
        for (ObjectId followerId : user.getFollowers()) {
            userRepository.findById(followerId).ifPresent((follower)->follower.getFollowings().remove(user.getId()));
        }
        for (ObjectId followingId : user.getFollowings()) {
            userRepository.findById(followingId).ifPresent((following) -> following.getFollowers().remove(user.getId()));
        }
        folderRepository.findById(user.getRootFolderId()).ifPresent((folder)->folderRepository.delete(folder));
        imageService.deleteImage(user.getImageId());
        userRepository.delete(user);
    }

    public List<FollowInfo> readFollowerSmall(String email) {
        User user = findOneEmail(email);
        List<ObjectId> followers = user.getFollowers();
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followerInfos;
        if(followers.size()<5){
            followerInfos=listToInfo(followers,followings);
        }else{
            followerInfos=listToInfo(followers.subList(0,5),followings);
        }
        return followerInfos;

    }

    public List<FollowInfo> readFollowingSmall(String email) {
        User user = findOneEmail(email);
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followingInfos;
        if(followings.size()<5){
            followingInfos=listToInfo(followings,followings);
        }else{
            followingInfos=listToInfo(followings.subList(0,5),followings);
        }
        return followingInfos;
    }


    private void validateEmailUser(String email){
        String regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new CustomException(ErrorCode.EMAIL_NOT_VALID);
        }
    }

//    private void validatePassword(String password){
//
//    }
}
