package PoolC.Comect.user.service;

import PoolC.Comect.exception.CustomException;
import PoolC.Comect.exception.ErrorCode;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.FollowInfo;
import PoolC.Comect.user.domain.MemberData;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.ReadFollowResponseDto;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.elasticsearch.monitor.os.OsStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //회원 가입
    @Transactional
    public ObjectId join(String email, String password,String nickname,String imageUrl){
        //validateEmailUser(email);
        validateDuplicateUser(email);
        validateDuplicateNickname(nickname);
        //validatePassword(password);
        Folder folder=new Folder("");
        folderRepository.save(folder);
        User user=new User(nickname,email,folder.get_id(),imageUrl, password);
        userRepository.save(user);
        return user.getId();
    }

    public void login(String email, String password){
        //validateEmailUser(email);
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.LOGIN_FAIL));
        if(!user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    public void update(String email,String userNickname, String picture){
        //validateEmailUser(email);
        User user = findOne(email);
        validateDuplicateNickname(userNickname);
        user.setNickname(userNickname);
        user.setImageUrl(picture);
        userRepository.save(user);
    }

    public User findOne(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void createFollow(String email, String followedNickname){
        User user = findOne(email);
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

    private void validateDuplicateUser(String email){
        if(!userRepository.findByEmail(email).isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    //나중에 닉네임 중복 조회 서비스에서 사용
    private void validateDuplicateNickname(String nickname){
        if(!userRepository.findByNickname(nickname).isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    public List<FollowInfo> readFollower(String email) {
        User user = findOne(email);
        List<ObjectId> followers = user.getFollowers();
        List<FollowInfo> followerInfos=listToInfo(followers);
        return followerInfos;
    }

    public List<FollowInfo> readFollowing(String email) {
        User user = findOne(email);
        List<ObjectId> followings = user.getFollowings();
        List<FollowInfo> followingInfos=listToInfo(followings);
        return followingInfos;
    }

    @Transactional
    public void deleteFollow(String email, String followedNickname) {
        User user = findOne(email);
        User followed = userRepository.findByNickname(followedNickname).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        user.getFollowings().remove(followed.getId());
        followed.getFollowers().remove(user.getId());
        userRepository.save(user);
        userRepository.save(followed);
    }

    private List<FollowInfo> listToInfo(List<ObjectId> list){
        List<FollowInfo> followInfos=new ArrayList<>();
        for (ObjectId followId : list) {
            userRepository.findById(followId).ifPresent((following)->{
                FollowInfo followInfo = FollowInfo.builder()
                        .email(following.getEmail())
                        .nickname(following.getNickname())
                        .imageUrl(following.getImageUrl())
                        .build();
                followInfos.add(followInfo);
            });
        }
        return followInfos;
    }




//    private void validateEmailUser(String email){
//        String regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
//        Pattern pattern = Pattern.compile(regx);
//        Matcher matcher = pattern.matcher(email);
//        if(!matcher.matches()){
//            throw new CustomException(ErrorCode.EMAIL_NOT_VALID);
//        }
//    }

//    private void validatePassword(String password){
//
//    }
}
