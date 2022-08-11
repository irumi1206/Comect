package PoolC.Comect.user;

import PoolC.Comect.domain.data.Data;
import PoolC.Comect.domain.user.User;
import PoolC.Comect.data.DataRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    //회원 가입
    @Transactional
    public ObjectId join(String userNickname, String email, String picture, String password){
        Data data=new Data();
        dataRepository.save(data);
        User user=new User(userNickname,email,data.getId(),picture, password);
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user){
        Optional<User> findUsers = userRepository.findByEmail(user.getEmail());
        if(!findUsers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }
    }

    @Transactional
    public void update(String email,String userNickname, String picture){
        User user = userRepository.findByEmail(email).get();
        user.setUserNickname(userNickname);
        user.setPicture(picture);
        userRepository.save(user);
    }

    public User findOne(String email){
        return userRepository.findByEmail(email).get();
    }
}
