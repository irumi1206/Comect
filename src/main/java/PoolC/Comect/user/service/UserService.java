package PoolC.Comect.user.service;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    //회원 가입
    @Transactional
    public ObjectId join(String email, String password,String userNickname,String picture) throws InterruptedException {
        validateEmailUser(email);
        validateDuplicateUser(email);
        Data data=new Data();
        dataRepository.save(data);
        User user=new User(userNickname,email,data.getId(),picture, password);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(String email) throws InterruptedException {
        Optional<User> findUsers = userRepository.findByEmail(email);
        if(!findUsers.isEmpty()){
            throw new InterruptedException("이미 존재하는 이메일입니다");
        }
    }

    private void validateEmailUser(String email) throws InterruptedException {
        String regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new InterruptedException("형식에 맞지 않는 이메일입니다");
        }
    }

    @Transactional
    public void update(String email,String userNickname, String picture){
        Optional<User> userOption = userRepository.findByEmail(email);
        if(userOption.isEmpty()){
            throw new NoSuchElementException("해당 이메일의 유저가 없습니다.");
        }else {
            User user = userOption.get();
            user.setUserNickname(userNickname);
            user.setPicture(picture);
            userRepository.save(user);
        }
    }

    public User findOne(String email){
        Optional<User> userOption = userRepository.findByEmail(email);
        if(userOption.isEmpty()){
            throw new NoSuchElementException("해당 이메일의 유저가 없습니다.");
        }
        User user = userOption.get();
        return user;
    }
    public User findById(ObjectId id){
        Optional<User> userOption = userRepository.findById(id);
        if(userOption.isEmpty()){
            throw new NoSuchElementException("해당 아이디의 유저가 없습니다.");
        }
        User user = userOption.get();
        return user;
    }
}
