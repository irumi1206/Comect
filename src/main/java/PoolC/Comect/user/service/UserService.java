package PoolC.Comect.user.service;

import PoolC.Comect.common.CustomException;
import PoolC.Comect.common.ErrorCode;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
        validateEmailUser(email);
        validateDuplicateUser(email);
        validatePassword(password);
        Folder folder=new Folder("");
        folderRepository.save(folder);
        User user=new User(nickname,email,folder.get_id(),imageUrl, password);
        userRepository.save(user);
        return user.getId();
    }

    public void login(String email, String password){
        validateEmailUser(email);
        Optional<User> userOption = userRepository.findByEmail(email);
        if(userOption.isEmpty()){
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
        User user = userOption.get();
        if(!user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    private void validateDuplicateUser(String email){
        Optional<User> findUsers = userRepository.findByEmail(email);
        if(!findUsers.isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    //나중에 닉네임 중복 조회 서비스에서 사용
    private void validateDuplicateNickname(String nickname){
        Optional<User> findUsers = userRepository.findByNickname(nickname);
        if(!findUsers.isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    private void validateEmailUser(String email){
        String regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new CustomException(ErrorCode.EMAIL_NOT_VALID);
        }
    }

    private void validatePassword(String password){

    }

    public void update(String email,String userNickname, String picture){
        validateEmailUser(email);
        Optional<User> userOption = userRepository.findByEmail(email);
        if(userOption.isEmpty()){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }else {
            User user = userOption.get();
            user.setNickname(userNickname);
            user.setImageUrl(picture);
            userRepository.save(user);
        }
    }

    public User findOne(String email){
        validateEmailUser(email);
        Optional<User> userOption = userRepository.findByEmail(email);
        if(userOption.isEmpty()){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        User user = userOption.get();
        return user;
    }

}
