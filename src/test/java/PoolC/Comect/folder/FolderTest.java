package PoolC.Comect.folder;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class FolderTest {

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
    FolderService folderService;

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

    public void authorizedLogin(int number){
        System.out.println("유저 생성중");
        String email="user"+number+"@email.com";
        String password="user"+number+"Password";
        String nickname="user"+number+"Nickname";
        Boolean joinReturn=userService.join(email,passwordEncoder.encode(password),nickname,null);
        String userId=emailRepository.findByEmail("user"+number+"@email.com").get().getId().toString();
        emailService.emailCheck(userId);
    }

    @BeforeEach
    public void before() {
        deleteAllTestData();
        authorizedLogin(1);
        authorizedLogin(2);
    }

    public void createFolder(){
        folderService.folderCreate("user1@email.com","","운영체제");
        folderService.folderCreate("user1@email.com","운영체제/","인터럽트");
        folderService.folderCreate("user1@email.com","운영체제/","프로세스");
        folderService.folderCreate("user1@email.com","운영체제/","시스템 콜");
        folderService.folderCreate("user1@email.com","운영체제/프로세스/","스레드");
        folderService.folderCreate("user1@email.com","운영체제/프로세스/","linux task");
        folderService.folderCreate("user1@email.com","운영체제/프로세스/","cfs 스케줄러");
    }

    @Test
    @DisplayName("1. 폴더생성성공")
    public void 폴더생성성공(){

        createFolder();

        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/인터럽트/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/시스템 콜/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/스레드/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/linux task/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/cfs 스케줄러/")).isEqualTo(true);

    }

    @Test
    @DisplayName("1. 폴더생성실패, 동일한 폴더이름")
    public void 폴더생성실패_동일한폴더이름(){

        createFolder();
        CustomException customException = assertThrows(CustomException.class,()->folderService.folderCreate("user1@email.com","운영체제/프로세스/","cfs 스케줄러"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FILE_CONFLICT);

    }

    @Test
    @DisplayName("1. 폴더생성실패, 폴더 경로 존재하지 않음")
    public void 폴더생성실패_폴더경로존재하지않음(){

        createFolder();
        CustomException customException = assertThrows(CustomException.class,()->folderService.folderCreate("user1@email.com","운영체제/프로 세스/","cfs 스케줄러"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("2. 폴더읽기")
    public void 폴더읽기(){

    }

    @Test
    @DisplayName("3. 폴더수정")
    public void 폴더수정(){

    }

    @Test
    @DisplayName("4. 폴더삭제")
    public void 폴더삭제(){


    }

    @Test
    @DisplayName("5. 링크생성")
    public void 링크생성(){

    }

    @Test
    @DisplayName("6. 링크읽기")
    public void 링크읽기(){

    }

    @Test
    @DisplayName("7. 링크수정")
    public void 링크수정(){

    }

    @Test
    @DisplayName("8. 링크삭제")
    public void 링크삭제(){

    }

}
