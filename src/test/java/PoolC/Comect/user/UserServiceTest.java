package PoolC.Comect.user;

import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ElasticUserRepository elasticUserRepository;
    @Autowired
    PasswordChangeEmailRepository passwordChangeEmailRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    ElasticFolderRepository elasticFolderRepository;
    @Autowired
    ElasticLinkRepository elasticLinkRepository;

    @Autowired
    EmailService emailService;
    @Autowired
    ImageService imageService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void deleteAllTestData(){
        userRepository.deleteAll();
        folderRepository.deleteAll();
        elasticUserRepository.deleteAll();
        passwordChangeEmailRepository.deleteAll();
        emailRepository.deleteAll();
    }

    public void authorizedLogin(int number){
        System.out.println("유저 생성중");
        String email="user"+number+"@email.com";
        String password="user"+number+"Password";
        String nickname="user"+number+"Nickname";
        Boolean joinReturn=userService.join(email,password,nickname,null);
        String userId=emailRepository.findByEmail("user"+number+"@email.com").get().getId().toString();
        emailService.emailCheck(userId);
    }

    @Before
    public void before() {
        deleteAllTestData();
        authorizedLogin(1);
        authorizedLogin(2);
//        authorizedLogin(3);
//        authorizedLogin(4);
//        authorizedLogin(5);
    }


    @Test
    public void join() {

    }

//    @Test
//    void passwordChange() {
//    }
//
//    @Test
//    void passwordChangeCheck() {
//    }
//
//    @Test
//    void login() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void findOneEmail() {
//    }
//
//    @Test
//    void findOneNickname() {
//    }
//
//    @Test
//    void findOneId() {
//    }
//
//    @Test
//    void createFollowNickname() {
//    }
//
//    @Test
//    void createFollowEmail() {
//    }
//
//    @Test
//    void getMemberInfo() {
//    }
//
//    @Test
//    void getMemberInfoByEmail() {
//    }
//
//    @Test
//    void isFollower() {
//    }
//
//    @Test
//    void readFollowingById() {
//    }
//
//    @Test
//    void readFollowedById() {
//    }
//
//    @Test
//    void validateDuplicateUser() {
//    }
//
//    @Test
//    void readFollower() {
//    }
//
//    @Test
//    void readFollowing() {
//    }
//
//    @Test
//    void deleteFollowNickname() {
//    }
//
//    @Test
//    void deleteFollowEmail() {
//    }
//
//    @Test
//    void deleteMember() {
//    }
//
//    @Test
//    void readFollowerSmall() {
//    }
//
//    @Test
//    void readFollowingSmall() {
//    }
//
//    @Test
//    void test1() {
//    }
}