package PoolC.Comect.user.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticUser.domain.ElasticUser;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.email.PasswordChangeEmail;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.*;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final ImageService imageService;
    private final ElasticUserRepository elasticUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordChangeEmailRepository passwordChangeEmailRepository;

    //회원 가입
//    @Transactional
    public boolean join(String email, String password, String nickname, MultipartFile multipartFile){
        //validation check
//        validateEmailUser(email);
        validateDuplicateUser(email);
        validateDuplicateNickname(nickname);
        //validatePassword(password);
        emailService.emailSend(email);

        //이미지 저장
        User user=new User(nickname,email,password);
        ImageUploadData imageUploadData = imageService.createImage(multipartFile, email,user.getId().toString());
        user.setImageUrl(imageUploadData.getImageUrl());
        userRepository.save(user);

        //es에 저장
//        ElasticUser elasticUser=new ElasticUser(user.getId().toString(),nickname);
//        elasticUserRepository.save(elasticUser);

        return imageUploadData.isSuccess();
    }

    public void passwordChange(String email){
        emailService.passwordChangeEmailSend(email);
    }

    public void passwordChangeCheck(String email, int randomNumber, String newPassword){
        PasswordChangeEmail passwordChangeEmail = passwordChangeEmailRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.EMAIL_AUTH_FAILED));
        if(passwordChangeEmail.getRandomNumber()==randomNumber && passwordChangeEmail.isValid()){
            passwordChangeEmailRepository.delete(passwordChangeEmail);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            user.setPassword(newPassword);
            userRepository.save(user);
            System.out.println("password changed!");
        }else{
            throw new CustomException(ErrorCode.EMAIL_AUTH_FAILED);
        }
    }

    public User login(String email, String password){
        //validateEmailUser(email);
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.LOGIN_FAIL));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
        if(!user.getRoles().contains("ROLE_AU")){
            throw new CustomException(ErrorCode.EMAIL_AUTH_FAILED);
        }
        return user;
    }

    public boolean update(String email,String userNickname, MultipartFile newMultipartFile, String imageChange){
        //validateEmailUser(email);
        User user = findOneEmail(email);
        if(!user.getNickname().equals(userNickname)){
            validateDuplicateNickname(userNickname);
            user.setNickname(userNickname);
            //es update
            System.out.println(userNickname);
            System.out.println(user.getId().toString());
            Optional<ElasticUser> elasticUser=elasticUserRepository.findByUserId(user.getId().toString());
            elasticUser.ifPresent(
                    currentUser->{
                        System.out.println(currentUser);
                        currentUser.setNickname(userNickname);
                        elasticUserRepository.save(currentUser);
                    }
            );
        }
        boolean changeSuccess;
        if(imageChange.equals("true")){
            ObjectId postImageId=user.getImageId();
            ImageUploadData imageUploadData = imageService.createImage(newMultipartFile, email,user.getId().toString());
            changeSuccess = imageUploadData.isSuccess();
            if(changeSuccess){
                imageService.deleteImage(postImageId,user.getId().toString());
                user.setImageUrl(imageUploadData.getImageUrl());
            }
        }else{
            changeSuccess=true;
        }

        userRepository.save(user);
        return changeSuccess;
    }

    public User findOneEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!user.getRoles().contains("ROLE_AU")) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        return user;
    }

    public User findOneNickname(String nickname){
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!user.getRoles().contains("ROLE_AU")) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        return user;
    }

    public User findOneId(ObjectId id){
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!user.getRoles().contains("ROLE_AU")) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        return user;
    }

    //@Transactional
    public FollowInfo createFollowNickname(String email, String followedNickname){
        User user = findOneEmail(email);
        if(user.getNickname().equals(followedNickname)) throw new CustomException(ErrorCode.FOLLOW_ME);
        User followed = findOneNickname(followedNickname);
        if(user.getFollowings().contains(followed.getId())){
            throw new CustomException(ErrorCode.FOLLOWING_EXIST);
        }
        user.getFollowings().add(followed.getId());
        followed.getFollowers().add(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
        FollowInfo followInfo = FollowInfo.builder()
                .isFollowing(true)
                .imageUrl(followed.getImageUrl())
                .email(followed.getEmail())
                .nickname(followed.getNickname())
                .build();
        return followInfo;
    }

    //@Transactional
    public FollowInfo createFollowEmail(String email, String followedEmail){
        if(email.equals(followedEmail)) throw new CustomException(ErrorCode.FOLLOW_ME);
        User user = findOneEmail(email);
        User followed = findOneEmail(followedEmail);
        if(user.getFollowings().contains(followed.getId())){
            throw new CustomException(ErrorCode.FOLLOWING_EXIST);
        }
        user.getFollowings().add(followed.getId());
        followed.getFollowers().add(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
        FollowInfo followInfo = FollowInfo.builder()
                .isFollowing(true)
                .imageUrl(followed.getImageUrl())
                .email(followed.getEmail())
                .nickname(followed.getNickname())
                .build();
        return followInfo;
    }

    public MemberData getMemberInfo(String email, String nickname){
        User user = findOneEmail(email);
        User other = findOneNickname(nickname);
        List<ObjectId> followings = user.getFollowings();
        MemberData memberData = MemberData.builder()
                .follower(other.getFollowers().size())
                .following(other.getFollowings().size())
                .nickname(other.getNickname())
                .imageUrl(other.getImageUrl())
                .isFollowing(followings.contains(other.getId()))
                .build();
        return memberData;
    }

    public MemberData getMemberInfoByEmail(String email, String otherEmail){
        User user = findOneEmail(email);
        User other = findOneEmail(otherEmail);
        List<ObjectId> followings = user.getFollowings();
        MemberData memberData = MemberData.builder()
                .imageUrl(other.getImageUrl())
                .nickname(other.getNickname())
                .following(other.getFollowings().size())
                .follower(other.getFollowers().size())
                .isFollowing(followings.contains(other.getId()))
                .build();
        return memberData;
    }

    //user가 followed를 팔로우 중인지 확인하는 함수
    public boolean isFollower(ObjectId id, ObjectId followedId){
        User user = findOneId(id);
        User followed = findOneId(followedId);
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
    public void deleteFollowNickname(String email, String followedNickname) {
        User user = findOneEmail(email);
        User followed = findOneNickname(followedNickname);
        user.getFollowings().remove(followed.getId());
        followed.getFollowers().remove(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
    }

    //@Transactional
    public void deleteFollowEmail(String email, String followedEmail) {
        User user = findOneEmail(email);
        User followed = findOneEmail(followedEmail);
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
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
        for (ObjectId followerId : user.getFollowers()) {
            userRepository.findById(followerId).ifPresent((follower)->{
                follower.getFollowings().remove(user.getId());
                userRepository.save(follower);
            });
        }
        for (ObjectId followingId : user.getFollowings()) {
            userRepository.findById(followingId).ifPresent((following) ->{
                following.getFollowers().remove(user.getId());
                userRepository.save(following);
            });
        }
        folderRepository.findById(user.getRootFolderId()).ifPresent((folder)->folderRepository.delete(folder));
        imageService.deleteImage(user.getImageId(),user.getId().toString());
        userRepository.delete(user);
        //es에서 제거
        elasticUserRepository.deleteByUserId(user.getId().toString());
    }

    public ReadFollowerData readFollowerSmall(String email) {
        ReadFollowerData readFollowerData = new ReadFollowerData();
        User user = findOneEmail(email);
        List<ObjectId> followers = user.getFollowers();
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followerInfos;
        if(followers.size()<5){
            followerInfos=listToInfo(followers,followings);
        }else{
            followerInfos=listToInfo(followers.subList(0,5),followings);
        }
        readFollowerData.setNumberOfFollowers(followers.size());
        readFollowerData.setFollowers(followerInfos);
        return readFollowerData;

    }

    public ReadFollowingData readFollowingSmall(String email) {
        ReadFollowingData readFollowingData = new ReadFollowingData();
        User user = findOneEmail(email);
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followingInfos;
        if(followings.size()<5){
            followingInfos=listToInfo(followings,followings);
        }else{
            followingInfos=listToInfo(followings.subList(0,5),followings);
        }
        readFollowingData.setNumberOfFollowings(followings.size());
        readFollowingData.setFollowings(followingInfos);
        return readFollowingData;
    }


}
