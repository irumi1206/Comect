package PoolC.Comect.elasticUser;

import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.elasticUser.service.ElasticUserService;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.user.domain.FollowInfo;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ElasticUserTest {

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
    UserService userService;
    @Autowired
    ElasticUserService elasticUserService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void deleteAllTestData(){
        userRepository.deleteAll();
        folderRepository.deleteAll();
        elasticUserRepository.deleteAll();
        passwordChangeEmailRepository.deleteAll();
        emailRepository.deleteAll();
        imageRepository.deleteAll();
        elasticFolderRepository.deleteAll();
        elasticLinkRepository.deleteAll();
    }

    public void authorizedLogin(int number,String injectedNickname){
        System.out.println("유저 생성중");
        String email="user"+number+"@email.com";
        String password="user"+number+"Password";
        String nickname=injectedNickname;
        Boolean joinReturn=userService.join(email,passwordEncoder.encode(password),nickname,null);
        String userId=emailRepository.findByEmail("user"+number+"@email.com").get().getId().toString();
        emailService.emailCheck(userId);
    }

    @BeforeEach
    public void before() {
        deleteAllTestData();
        authorizedLogin(1,"sungha the mighty");
        authorizedLogin(2,"김성하");
        authorizedLogin(3,"헤엄치는 가물치");
        authorizedLogin(4,"백준 푸는 프로그래머");
    }

    public void constructRelationship(){
        FollowInfo followInfo;
        followInfo=userService.createFollowEmail("user1@email.com","user2@email.com");
        followInfo=userService.createFollowEmail("user1@email.com","user3@email.com");
    }

    @Test
    @DisplayName("유저검색 성공, 키워드로 유저 검색 성공")
    public void 유저검색성공(){

        constructRelationship();
        assertThat(elasticUserService.searchUser("헤엄치는","user1@email.com").get(0).getNickname()).isEqualTo("헤엄치는 가물치");
        assertThat(elasticUserService.searchUser("김","user1@email.com").get(0).getNickname()).isEqualTo("김성하");
        assertThat(elasticUserService.searchUser("백","user1@email.com").get(0).getNickname()).isEqualTo("백준 푸는 프로그래머");

    }
}
