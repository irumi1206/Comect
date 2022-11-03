package PoolC.Comect.user;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticUser.domain.ElasticUser;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.*;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.*;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
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
        imageRepository.deleteAll();
        elasticFolderRepository.deleteAll();
        elasticLinkRepository.deleteAll();
    }

    public void authorizedLogin(int number){
        System.out.println("유저 생성중");
        String email="user"+number+"@email.com";
        String password="user"+number+"Password";
        String nickname="user"+number+"Nickname";
        Boolean joinReturn=userService.join(email, passwordEncoder.encode(password),nickname,null);
        String userId=emailRepository.findByEmail("user"+number+"@email.com").get().getId().toString();
        emailService.emailCheck(userId);
    }

    @BeforeEach
    public void before() {
        deleteAllTestData();
        authorizedLogin(1);
//        authorizedLogin(2);
//        authorizedLogin(3);
//        authorizedLogin(4);
//        authorizedLogin(5);
    }


    @Test
    public void 회원가입1_정상작동() {
        boolean testNickname = userService.join("testUser@naver.com", passwordEncoder.encode("123456"), "testNickname", null);
        assertThat(testNickname).isEqualTo(true);
        Email email = emailRepository.findByEmail("testUser@naver.com").orElseThrow();
        assertThat(email.getEmail()).isEqualTo("testUser@naver.com");
        User user = userRepository.findByEmail("testUser@naver.com").orElseThrow();
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        assertThat(user.getRoles()).isNotEqualTo(user1.getRoles());
    }
//    @Test
//    public void 회원가입2_이메일Regex() {
////        userService.join("testUser", "123456", "testNickname", null);
//        CustomException customException = assertThrows(CustomException.class, () -> userService.join("testUser", "123456", "testNickname", null));
//        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.EMAIL_NOT_VALID);
//    }
//    @Test
//    public void 회원가입3_이메일없음() {
//        CustomException customException = assertThrows(CustomException.class,()->userService.join(null, "123456", "testNickname", null));
//        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.EMAIL_NOT_VALID);
//    }
    @Test
    public void 회원가입4_이메일겹침() {
        CustomException customException = assertThrows(CustomException.class,()->userService.join("user1@email.com", "123456", "testNickname", null));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.EMAIL_EXISTS);
    }
    @Test
    public void 회원가입5_닉네임겹침() {
        CustomException customException = assertThrows(CustomException.class,()->userService.join("user@email.com", "123456", "user1Nickname", null));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.NICKNAME_EXISTS);
    }

    @Test
    public void 비밀번호변경1_정상작동() {
        userService.login("user1@email.com","user1Password");
        userService.passwordChange("user1@email.com");
        PasswordChangeEmail passwordChangeEmail = passwordChangeEmailRepository.findByEmail("user1@email.com").orElseThrow();
        userService.passwordChangeCheck("user1@email.com",passwordChangeEmail.getRandomNumber(),passwordEncoder.encode("password"));
        userService.login("user1@email.com","password");
        passwordChangeEmailRepository.findByEmail("user1@email.com").ifPresent((temp)->new CustomException(ErrorCode.EMAIL_SEND_FAIL));
    }

    @Test
    public void 비밀번호변경2_변경전비밀번호로그인시도() {
        userService.login("user1@email.com","user1Password");
        userService.passwordChange("user1@email.com");
        PasswordChangeEmail passwordChangeEmail = passwordChangeEmailRepository.findByEmail("user1@email.com").orElseThrow();
        userService.passwordChangeCheck("user1@email.com",passwordChangeEmail.getRandomNumber(),passwordEncoder.encode("password"));
        CustomException customException = assertThrows(CustomException.class,()->userService.login("user1@email.com","user1Password"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.LOGIN_FAIL);
    }

    @Test
    public void 비밀번호변경3_인증실패() {
        userService.login("user1@email.com","user1Password");
        userService.passwordChange("user1@email.com");
        PasswordChangeEmail passwordChangeEmail = passwordChangeEmailRepository.findByEmail("user1@email.com").orElseThrow();
        CustomException customException = assertThrows(CustomException.class,()->userService.passwordChangeCheck("user1@email.com",passwordChangeEmail.getRandomNumber()+5,passwordEncoder.encode("password")));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.EMAIL_AUTH_FAILED);
    }

    @Test
    public void 로그인1_성공() {
        userService.login("user1@email.com", "user1Password");
    }

    @Test
    public void 로그인2_이메일없음() {
        CustomException customException = assertThrows(CustomException.class,()->userService.login("user2@email.com", "user1Password"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.LOGIN_FAIL);
    }

    @Test
    public void 로그인3_비밀번호틀림() {
        CustomException customException = assertThrows(CustomException.class,()->userService.login("user1@email.com", "user1Passwordasdf"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.LOGIN_FAIL);
    }

    @Test
    public void 로그인4_인증안된유저() {
        boolean testNickname = userService.join("testUser@naver.com", passwordEncoder.encode("123456"), "testNickname", null);
        CustomException customException = assertThrows(CustomException.class,()->userService.login("testUser@naver.com", "123456"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.EMAIL_AUTH_FAILED);
    }

    @Test
    public void 개인정보수정1_정상작동_닉네임() {
        boolean update = userService.update("user1@email.com", "nick", null, "false");
        User user = userRepository.findByEmail("user1@email.com").orElseThrow();
        ElasticUser elasticUser = elasticUserRepository.findByUserId(user.getId().toString()).orElseThrow();
        assertThat(user.getNickname()).isEqualTo("nick");
        assertThat(elasticUser.getNickname()).isEqualTo("nick");
        assertThat(update).isEqualTo(true);
    }

    @Test
    public void 개인정보수정2_정상작동_이미지() {
        boolean update = userService.update("user1@email.com", "user1Nickname", null, "true");
        User user = userRepository.findByEmail("user1@email.com").orElseThrow();
        assertThat(user.getNickname()).isEqualTo("user1Nickname");
        assertThat(update).isEqualTo(true);
    }

    @Test
    public void 팔로우_닉네임으로() {
        authorizedLogin(2);
        FollowInfo user2Info = userService.createFollowNickname("user1@email.com", "user2Nickname");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().get(0)).isEqualTo(user2.getId());
        assertThat(user2.getFollowers().get(0)).isEqualTo(user1.getId());
        assertThat(user2Info.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user2Info.getImageUrl()).isEqualTo(user2.getImageUrl());
        assertThat(user2Info.getNickname()).isEqualTo(user2.getNickname());
        assertThat(user2Info.getIsFollowing()).isEqualTo(true);
    }

    @Test
    public void 팔로우_닉네임으로_없는유저() {
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowNickname("user1@email.com", "user3Nickname"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 팔로우_닉네임으로_나를팔로우() {
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowNickname("user1@email.com", "user1Nickname"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FOLLOW_ME);
    }

    @Test
    public void 팔로우_닉네임으로_인증안됨() {
        boolean testNickname = userService.join("testUser@naver.com", passwordEncoder.encode("123456"), "testNickname", null);
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowNickname("user1@email.com", "testNickname"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 팔로우_닉네임으로_이미팔로우() {
        authorizedLogin(2);
        FollowInfo user2Info = userService.createFollowNickname("user1@email.com", "user2Nickname");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().get(0)).isEqualTo(user2.getId());
        assertThat(user2.getFollowers().get(0)).isEqualTo(user1.getId());
        assertThat(user2Info.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user2Info.getImageUrl()).isEqualTo(user2.getImageUrl());
        assertThat(user2Info.getNickname()).isEqualTo(user2.getNickname());
        assertThat(user2Info.getIsFollowing()).isEqualTo(true);
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowNickname("user1@email.com", "user2Nickname"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FOLLOWING_EXIST);
    }



    @Test
    public void 팔로우_이메일로() {
        authorizedLogin(2);
        FollowInfo user2Info = userService.createFollowEmail("user1@email.com", "user2@email.com");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().get(0)).isEqualTo(user2.getId());
        assertThat(user2.getFollowers().get(0)).isEqualTo(user1.getId());
        assertThat(user2Info.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user2Info.getImageUrl()).isEqualTo(user2.getImageUrl());
        assertThat(user2Info.getNickname()).isEqualTo(user2.getNickname());
        assertThat(user2Info.getIsFollowing()).isEqualTo(true);
    }

    @Test
    public void 팔로우_이메일로_없는유저() {
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowEmail("user1@email.com", "user3@email.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 팔로우_이메일로_나를팔로우() {
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowEmail("user1@email.com", "user1@email.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FOLLOW_ME);
    }

    @Test
    public void 팔로우_이메일로_인증안됨() {
        boolean testNickname = userService.join("testUser@naver.com", passwordEncoder.encode("123456"), "testNickname", null);
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowEmail("user1@email.com", "testUser@naver.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 팔로우_이메일로_이미팔로우() {
        authorizedLogin(2);
        FollowInfo user2Info = userService.createFollowEmail("user1@email.com", "user2@email.com");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().get(0)).isEqualTo(user2.getId());
        assertThat(user2.getFollowers().get(0)).isEqualTo(user1.getId());
        assertThat(user2Info.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user2Info.getImageUrl()).isEqualTo(user2.getImageUrl());
        assertThat(user2Info.getNickname()).isEqualTo(user2.getNickname());
        assertThat(user2Info.getIsFollowing()).isEqualTo(true);
        CustomException customException = assertThrows(CustomException.class,()->userService.createFollowEmail("user1@email.com", "user2@email.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.FOLLOWING_EXIST);
    }

    @Test
    public void 유저조회_닉네임_정상() {
        authorizedLogin(2);
        MemberData user2Info = userService.getMemberInfo("user1@email.com", "user2Nickname");
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user2Info.getNickname()).isEqualTo("user2Nickname");
        assertThat(user2Info.getImageUrl()).isEqualTo("");
        assertThat(user2Info.getIsFollowing()).isEqualTo(false);
        assertThat(user2Info.getFollower()).isEqualTo(0);

        userService.createFollowEmail("user1@email.com", "user2@email.com");
        MemberData user2Info2 = userService.getMemberInfo("user1@email.com", "user2Nickname");
        User user22 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user2Info2.getNickname()).isEqualTo("user2Nickname");
        assertThat(user2Info2.getImageUrl()).isEqualTo("");
        assertThat(user2Info2.getIsFollowing()).isEqualTo(true);
        assertThat(user2Info2.getFollower()).isEqualTo(1);
    }

    @Test
    public void 유저조회_닉네임_없는유저(){
        CustomException customException = assertThrows(CustomException.class,()->userService.getMemberInfo("user1@email.com", "user2Nickname"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 유저조회_이메일_정상() {
        authorizedLogin(2);
        MemberData user2Info = userService.getMemberInfoByEmail("user1@email.com", "user2@email.com");
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user2Info.getNickname()).isEqualTo("user2Nickname");
        assertThat(user2Info.getImageUrl()).isEqualTo("");
        assertThat(user2Info.getIsFollowing()).isEqualTo(false);
        assertThat(user2Info.getFollower()).isEqualTo(0);

        userService.createFollowEmail("user1@email.com", "user2@email.com");
        MemberData user2Info2 = userService.getMemberInfoByEmail("user1@email.com", "user2@email.com");
        User user22 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user2Info2.getNickname()).isEqualTo("user2Nickname");
        assertThat(user2Info2.getImageUrl()).isEqualTo("");
        assertThat(user2Info2.getIsFollowing()).isEqualTo(true);
        assertThat(user2Info2.getFollower()).isEqualTo(1);
    }

    @Test
    public void 유저조회_이메일_없는유저(){
        CustomException customException = assertThrows(CustomException.class,()->userService.getMemberInfoByEmail("user1@email.com", "user2@email.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 유저조회_이메일_인증안됨(){
        boolean testNickname = userService.join("testUser@naver.com", passwordEncoder.encode("123456"), "testNickname", null);
        CustomException customException = assertThrows(CustomException.class,()->userService.getMemberInfoByEmail("user1@email.com", "testUser@naver.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 팔로워_조회() {
        authorizedLogin(2);
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        List<FollowInfo> followInfos = userService.readFollower("user2@email.com");
        assertThat(followInfos.size()).isEqualTo(1);
        assertThat(followInfos.get(0).getIsFollowing()).isEqualTo(false);
        assertThat(followInfos.get(0).getEmail()).isEqualTo("user1@email.com");
        assertThat(followInfos.get(0).getImageUrl()).isEqualTo("");
        assertThat(followInfos.get(0).getNickname()).isEqualTo("user1Nickname");

        List<FollowInfo> followInfos2 = userService.readFollower("user1@email.com");
        assertThat(followInfos2.size()).isEqualTo(0);
    }

    @Test
    public void 팔로잉_조회() {
        authorizedLogin(2);
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        List<FollowInfo> followInfos = userService.readFollowing("user1@email.com");
        assertThat(followInfos.size()).isEqualTo(1);
        assertThat(followInfos.get(0).getIsFollowing()).isEqualTo(true);
        assertThat(followInfos.get(0).getEmail()).isEqualTo("user2@email.com");
        assertThat(followInfos.get(0).getImageUrl()).isEqualTo("");
        assertThat(followInfos.get(0).getNickname()).isEqualTo("user2Nickname");

        List<FollowInfo> followInfos2 = userService.readFollowing("user2@email.com");
        assertThat(followInfos2.size()).isEqualTo(0);
    }

    @Test
    public void 팔로우취소_닉네임_정상작동() {
        authorizedLogin(2);
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().size()).isEqualTo(1);
        assertThat(user2.getFollowers().size()).isEqualTo(1);
        userService.deleteFollowNickname("user1@email.com","user2Nickname");
        user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().size()).isEqualTo(0);
        assertThat(user2.getFollowers().size()).isEqualTo(0);
    }

    @Test
    public void 팔로우취소_이메일_정상작동() {
        authorizedLogin(2);
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().size()).isEqualTo(1);
        assertThat(user2.getFollowers().size()).isEqualTo(1);
        userService.deleteFollowEmail("user1@email.com","user2@email.com");
        user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().size()).isEqualTo(0);
        assertThat(user2.getFollowers().size()).isEqualTo(0);
    }

    @Test
    public void 회원탈퇴(){
        authorizedLogin(2);
        User user = userRepository.findByEmail("user2@email.com").orElseThrow();
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        userService.createFollowEmail("user2@email.com", "user1@email.com");
        user = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user.getFollowers().size()).isEqualTo(1);
        userService.deleteMember("user1@email.com","user1Password");
        user = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user.getFollowers().size()).isEqualTo(0);
        CustomException customException = assertThrows(CustomException.class,()->userService.findOneEmail("user1@email.com"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    public void 회원탈퇴_비밀번호실패(){
        CustomException customException = assertThrows(CustomException.class,()->userService.deleteMember("user1@email.com","user11Password"));
        assertThat(customException.getErrorCode()).isEqualByComparingTo(ErrorCode.LOGIN_FAIL);
    }

@Test
public void 팔로워_조회_작게() {
    authorizedLogin(2);
    userService.createFollowEmail("user1@email.com", "user2@email.com");
    userService.createFollowEmail("user2@email.com", "user1@email.com");
    User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
    User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
    assertThat(user1.getFollowings().size()).isEqualTo(1);
    assertThat(user2.getFollowers().size()).isEqualTo(1);
    ReadFollowerData readFollowerData = userService.readFollowerSmall("user2@email.com");
    assertThat(readFollowerData.getNumberOfFollowers()).isEqualTo(1);
}

    @Test
    public void 팔로잉_조회_작게() {
        authorizedLogin(2);
        userService.createFollowEmail("user1@email.com", "user2@email.com");
        userService.createFollowEmail("user2@email.com", "user1@email.com");
        User user1 = userRepository.findByEmail("user1@email.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@email.com").orElseThrow();
        assertThat(user1.getFollowings().size()).isEqualTo(1);
        assertThat(user2.getFollowers().size()).isEqualTo(1);
        ReadFollowingData readFollowingData = userService.readFollowingSmall("user1@email.com");
        assertThat(readFollowingData.getNumberOfFollowings()).isEqualTo(1);
    }
}