package PoolC.Comect.email;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticUser.domain.ElasticUser;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final PasswordChangeEmailRepository passwordChangeEmailRepository;
    private final UserRepository userRepository;
    private final RegisterMail registerMail;
    private final ElasticUserRepository elasticUserRepository;
    private final FolderRepository folderRepository;

    public void emailSend(String email) {
        validateDuplicateUser(email);

        emailRepository.findByEmail(email).ifPresent((emailAuth)->emailRepository.delete(emailAuth));
        Email emailAuth=new Email(email);
        registerMail.sendSimpleMessage(email,emailAuth.getId().toHexString());
        emailRepository.save(emailAuth);
    }

    public void passwordChangeEmailSend(String email){
        passwordChangeEmailRepository.findByEmail(email).ifPresent((emailAuth)->passwordChangeEmailRepository.delete(emailAuth));
        PasswordChangeEmail emailAuth=new PasswordChangeEmail(email);
        registerMail.sendSimplePasswordChangeMessage(email,emailAuth.getRandomNumber());
        passwordChangeEmailRepository.save(emailAuth);

    }

    public Email findOneEmail(String email){
        return emailRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.EMAIL_NOT_FOUND));
    }

    public Email findOneId(String id){
        return emailRepository.findById(new ObjectId(id)).orElseThrow(()->new CustomException(ErrorCode.EMAIL_NOT_FOUND));
    }

    public Boolean emailCheck(String id){
        Email emailAuth = findOneId(id);
        try{
            User user = userRepository.findByEmail(emailAuth.getEmail()).orElseThrow(() -> {
                throw new CustomException(ErrorCode.EMAIL_AUTH_FAILED);
            });
            emailAuthCheck(emailAuth.getEmail());
            user.setRoles(Collections.singletonList("ROLE_AU"));
            Folder folder=new Folder("");
            user.setRootFolderId(folder.get_id());
            folderRepository.save(folder);
            userRepository.save(user);
            emailRepository.delete(emailAuth);

            //es에 저장
            ElasticUser elasticUser=new ElasticUser(user.getId().toString(),user.getNickname());
            elasticUserRepository.save(elasticUser);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void emailAuthCheck(String email){
        Email emailAuth = findOneEmail(email);
        if(!emailAuth.isValid()){
            throw new CustomException(ErrorCode.EMAIL_AUTH_FAILED);
        }
    }

    public void validateDuplicateUser(String email){
        if(!userRepository.findByEmail(email).isEmpty()){
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }
}
