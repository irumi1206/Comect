package PoolC.Comect.folder;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    String link0Id;
    String link1Id;
    String link2Id;
    String link3Id;

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

    public void createLink(){

        folderService.linkCreate("user1@email.com","","링크 1","url1",null,null,"true",null);
        folderService.linkCreate("user1@email.com","운영체제/프로세스/스레드/","링크 1","url1",null,null,"true",null);
        folderService.linkCreate("user1@email.com","운영체제/프로세스/스레드/","링크 2","url1",null,null,"true",null);
        folderService.linkCreate("user1@email.com","운영체제/프로세스/스레드/","링크 3","url1",null,null,"true",null);

        Folder folder=folderService.folderRead("user1@email.com","운영체제/프로세스/스레드/");
        Folder rootFolder=folderService.folderRead("user1@email.com","");
        link0Id=rootFolder.getLinks().get(0).get_id().toString();
        link1Id=folder.getLinks().get(0).get_id().toString();
        link2Id=folder.getLinks().get(1).get_id().toString();
        link3Id=folder.getLinks().get(2).get_id().toString();
    }

    @Test
    @DisplayName("1. 폴더생성성공")
    public void 폴더생성성공(){

        createFolder();

        assertThat(folderService.folderCheckPath("user1@email.com","")).isEqualTo(true);
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

        CustomException customException1 = assertThrows(CustomException.class,()->folderService.folderCreate("user1@email.com","","운영체제"));
        assertThat(customException1.getErrorCode()).isEqualByComparingTo(ErrorCode.FILE_CONFLICT);
        CustomException customException2 = assertThrows(CustomException.class,()->folderService.folderCreate("user1@email.com","운영체제/프로세스/","cfs 스케줄러"));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.FILE_CONFLICT);

    }

    @Test
    @DisplayName("1. 폴더생성실패, 폴더 경로 존재하지 않음")
    public void 폴더생성실패_폴더경로존재하지않음(){

        createFolder();

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderCreate("user1@email.com","운영체제/프로 세스/","cfs 스케줄러"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("2. 폴더읽기성공")
    public void 폴더읽기성공(){

        createFolder();

        assertThat(folderService.folderRead("user1@email.com","").getName()).isEqualTo("");
        assertThat(folderService.folderRead("user1@email.com","운영체제/").getName()).isEqualTo("운영체제");

    }

    @Test
    @DisplayName("2. 폴더읽기실패, 경로가 유효하지 않음")
    public void 폴더읽기실패_경로가유효하지않음(){

        createFolder();

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderRead("user1@email.com","운영체제/프로 세스/"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("3. 폴더수정성공")
    public void 폴더수정성공(){

        createFolder();

        folderService.folderUpdate("user1@email.com","운영체제/프로세스/스레드","새로운 스레드");

    }

    @Test
    @DisplayName("3. 폴더수정실패, 경로가 루트임")
    public void 폴더수정실패_경로가루트임(){

        createFolder();

        CustomException customException1 = assertThrows(CustomException.class,()->folderService.folderUpdate("user1@email.com","","새로운 폴더"));
        assertThat(customException1.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_VALID);
        CustomException customException2 = assertThrows(CustomException.class,()->folderService.folderUpdate("user1@email.com","운영체제/프로세스/스 레드","새로운 스레드"));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("3. 폴더수정실패, 경로가 유효하지 않음")
    public void 폴더수정실패_경로가유효하지않음(){

        createFolder();

        CustomException customException2 = assertThrows(CustomException.class,()->folderService.folderUpdate("user1@email.com","운영체제/프로세스/스 레드","새로운 스레드"));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("3. 폴더수정실패, 이미 존재하는 폴더이름")
    public void 폴더수정실패_이미존재하는폴더이름(){

        createFolder();

        CustomException customException2 = assertThrows(CustomException.class,()->folderService.folderUpdate("user1@email.com","운영체제/프로세스/스레드","linux task"));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.FILE_CONFLICT);

    }

    @Test
    @DisplayName("4. 폴더삭제성공")
    public void 폴더삭제성공(){

        createFolder();
        List<String> path=new ArrayList<>();
        path.add("운영체제/프로세스/");
        folderService.linkCreate("user1@email.com","운영체제/프로세스/","newlink","newurl",null,null,"true",null);
        folderService.folderDelete("user1@email.com",path);


        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(false);

    }

    @Test
    @DisplayName("4. 폴더삭제실패, 루트폴더")
    public void 폴더삭제실패_루트폴더(){

        createFolder();
        List<String> path=new ArrayList<>();
        path.add("");

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderDelete("user1@email.com",path));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_VALID);


    }

    @Test
    @DisplayName("4. 폴더삭제실패, 경로유효하지않음")
    public void 폴더삭제실패_경로유효하지않음(){

        createFolder();
        List<String> path=new ArrayList<>();
        path.add("운영체제/잘못된 경로/");

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderDelete("user1@email.com",path));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("4. 폴더삭제실패, transaction test")
    public void 폴더삭제살패_transaction_test(){

        createFolder();
        List<String> path=new ArrayList<>();
        path.add("운영체제/프로세스/");
        path.add("운영체제/프로세스/스레드");

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderDelete("user1@email.com",path));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(true);

    }

    @Test
    @DisplayName("5. 폴더이동성공")
    public void 폴더이동성공(){

        createFolder();
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("운영체제/프로세스/");
        String modifiedPath="운영체제/인터럽트/";
        folderService.folderMove("user1@email.com",originalPaths,modifiedPath);

        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(false);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/스레드/")).isEqualTo(false);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/linux task/")).isEqualTo(false);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/cfs 스케줄러/")).isEqualTo(false);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/인터럽트/프로세스/스레드/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/인터럽트/프로세스/linux task/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/인터럽트/프로세스/cfs 스케줄러/")).isEqualTo(true);

    }

    @Test
    @DisplayName("5. 폴더이동실패, 폴더경로가 유효하지 않음")
    public void 폴더이동실패_폴더경로유효하지않음(){

        createFolder();
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("운영체제/프로세스들/");
        String modifiedPath="운영체제/인터럽트/";

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderMove("user1@email.com",originalPaths,modifiedPath));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("5. 폴더이동실패, 이미 폴더이름이 존재")
    public void 폴더이동실패_이미폴더이름이존재(){

        createFolder();
        folderService.folderCreate("user1@email.com","운영체제/인터럽트/","프로세스");
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("운영체제/프로세스/");
        String modifiedPath="운영체제/인터럽트/";

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderMove("user1@email.com",originalPaths,modifiedPath));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FILE_CONFLICT);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/스레드")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/linux task")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/cfs 스케줄러")).isEqualTo(true);

    }

    @Test
    @DisplayName("5. 폴더이동실패, 상위에서하위로 이동 transaction test")
    public void 폴더이동실패_상위에서하위로이동_transaction(){

        createFolder();
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("운영체제/프로세스/");
        String modifiedPath="운영체제/프로레스/스레드";

        CustomException customException = assertThrows(CustomException.class,()->folderService.folderMove("user1@email.com",originalPaths,modifiedPath));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/스레드")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/linux task")).isEqualTo(true);
        assertThat(folderService.folderCheckPath("user1@email.com","운영체제/프로세스/cfs 스케줄러")).isEqualTo(true);

    }

    @Test
    @DisplayName("6. 링크생성성공")
    public void 링크생성성공(){

        createFolder();
        createLink();
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();

        assertThat(folderRepository.checkPathLink(id,"링크 1/")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 1/")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 2/")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 3/")).isEqualTo(true);

    }

    @Test
    @DisplayName("6. 링크생성실패, 경로유효하지않음 ")
    public void 링크생성실패_경로유효하지않음(){

        createFolder();
        createLink();
        CustomException customException = assertThrows(CustomException.class,()->folderService.linkCreate("user1@email.com","운영체제/프로 세스/","새로운 링크","new url",null,null,"true",null));

        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("7. 링크읽기성공")
    public void 링크읽기성공(){

        createFolder();
        createLink();

        assertThat(folderService.linkRead("user1@email.com","운영체제/프로세스/스레드/",link1Id).getName()).isEqualTo("링크 1");
        assertThat(folderService.linkRead("user1@email.com","운영체제/프로세스/스레드/",link2Id).getName()).isEqualTo("링크 2");
        assertThat(folderService.linkRead("user1@email.com","운영체제/프로세스/스레드/",link3Id).getName()).isEqualTo("링크 3");
        assertThat(folderService.linkRead("user1@email.com","",link0Id).getName()).isEqualTo("링크 1");

    }

    @Test
    @DisplayName("7. 링크읽기실패, 경로가유효하지않음")
    public void 링크읽기실패_경로가유효하지않음(){

        createFolder();
        createLink();

        CustomException customException1 = assertThrows(CustomException.class,()->folderService.linkRead("user1@email.com","운영체제/프로 세스/스레드/",link1Id));
        assertThat(customException1.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        CustomException customException2 = assertThrows(CustomException.class,()->folderService.linkRead("user1@email.com","",link1Id));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

    @Test
    @DisplayName("8. 링크수정성공")
    public void 링크수정성공(){

        createFolder();
        createLink();
        folderService.linkUpdate(link1Id,"user1@email.com","운영체제/프로세스/스레드/","new name1","new url1",null,null,"true","true",null);
        folderService.linkUpdate(link2Id,"user1@email.com","운영체제/프로세스/스레드/","new name2","new url2",null,null,"true","false",null);
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();

        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/new name1/")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 1/")).isEqualTo(false);
    }

    @Test
    @DisplayName("8. 링크수정실패, 경로가 유효하지 않음")
    public void 링크수정실패_경로가유효하지않음(){

        createFolder();
        createLink();
        CustomException customException = assertThrows(CustomException.class,()->folderService.linkUpdate(link1Id,"user1@email.com","운영체제/프로세 스/스레드/","new name","new url",null,null,"true","true","newImageUrl"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();

        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/new name/")).isEqualTo(false);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 1/")).isEqualTo(true);
    }

    @Test
    @DisplayName("9. 링크삭제성공")
    public void 링크삭제성공(){

         createFolder();
         createLink();
         List<String> ids=new ArrayList<>();
         ids.add(link1Id);
         ids.add(link2Id);
         folderService.linkDelete("user1@email.com","운영체제/프로세스/스레드/",ids);
         ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();

         assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 1")).isEqualTo(false);
         assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 2")).isEqualTo(false);
         assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 3")).isEqualTo(true);
    }

    @Test
    @DisplayName("9. 링크삭제실패, 경로 유효하지않음")
    public void 링크삭제실패_경로유효하지않음(){

        createFolder();
        createLink();
        List<String> ids=new ArrayList<>();
        ids.add(link0Id);
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();
        CustomException customException = assertThrows(CustomException.class,()->folderService.linkDelete("user1@email.com","운영체제/프로세스/스레드/",ids));

        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 1")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 2")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/프로세스/스레드/링크 3")).isEqualTo(true);
    }

    @Test
    @DisplayName("10. 링크이동성공")
    public void 링크이동성공(){

        createFolder();
        createLink();
        List<String> Ids=new ArrayList<>();
        Ids.add(link1Id);
        Ids.add(link2Id);
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();
        folderService.linkMove("user1@email.com","운영체제/프로세스/스레드/",Ids,"운영체제/인터럽트/");

        assertThat(folderRepository.checkPathLink(id,"운영체제/인터럽트/링크 1/")).isEqualTo(true);
        assertThat(folderRepository.checkPathLink(id,"운영체제/인터럽트/링크 2/")).isEqualTo(true);
    }

    @Test
    @DisplayName("10. 링크이동실패, 경로 유효하지 않음")
    public void 링크이동실패_경로유효하지않음(){

        createFolder();
        createLink();
        List<String> Ids=new ArrayList<>();
        Ids.add(link1Id);
        Ids.add(link2Id);
        ObjectId id=userRepository.findByEmail("user1@email.com").get().getRootFolderId();

        CustomException customException1 = assertThrows(CustomException.class,()->folderService.linkMove("user1@email.com","운영체제/프로세스/스레드/",Ids,"운영체제/인터럽트 /"));
        assertThat(customException1.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);
        CustomException customException2 = assertThrows(CustomException.class,()->folderService.linkMove("user1@email.com","운영체제/프로 세스/스레드/",Ids,"운영체제/인터럽트/"));
        assertThat(customException2.getErrorCode()).isEqualByComparingTo(ErrorCode.PATH_NOT_FOUND);

    }

}
