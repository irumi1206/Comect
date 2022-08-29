package PoolC.Comect.user;

import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.*;
import PoolC.Comect.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class userTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before(){
        folderRepository.deleteAll();
        userRepository.deleteAll();

        Folder folder1 = new Folder("");
        User user1 = new User("user1", "user1Email@naver.com", folder1.get_id(), "user1Picture", "123456");
        folderRepository.save(folder1);
        userRepository.save(user1);
        Folder data2 = new Folder("");
        User user2 = new User("user2", "user2Email@naver.com", data2.get_id(), "user2Picture", "567891");
        folderRepository.save(data2);
        userRepository.save(user2);
        Folder data3 = new Folder("");
        User user3 = new User("user3", "user3Email@naver.com", data3.get_id(), "user3Picture", "123456");
        folderRepository.save(data3);
        userRepository.save(user3);
        Folder data4 = new Folder("");
        User user4 = new User("user4", "user4Email@naver.com", data4.get_id(), "user4Picture", "1234565");
        folderRepository.save(data4);
        userRepository.save(user4);

    }


    @Test
    @DisplayName("테스트 01 : 유저 생성")
    public void 유저생성() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setEmail("user5Email@naver.com");
        createUserRequestDto.setNickname("userfive");
        createUserRequestDto.setPassword("123456");
        createUserRequestDto.setImageUrl("user5picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        User user5 = userRepository.findByNickname("userfive").get();
        assertThat(user5.getEmail()).isEqualTo("user5Email@naver.com");
        assertThat(user5.getPassword()).isEqualTo("123456");
        assertThat(user5.getImageUrl()).isEqualTo("user5picture");

    }
    @Test
    @DisplayName("테스트 02 : 유저 생성 시 같은 이메일 실패")
    public void 유저생성_이미있는이메일() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setEmail("user1Email@naver.com");
        createUserRequestDto.setNickname("testName");
        createUserRequestDto.setPassword("123456");
        createUserRequestDto.setImageUrl("user3picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        Optional<User> testName = userRepository.findByNickname("testName");
        assertThat(testName.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테스트 03 : 유저 생성 시 이메일 형식 실패")
    public void 유저생성_형식실패() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setEmail("user1Email");
        createUserRequestDto.setNickname("testName");
        createUserRequestDto.setPassword("123456");
        createUserRequestDto.setImageUrl("user3picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Optional<User> testName = userRepository.findByNickname("testName");
        assertThat(testName.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테스트 04 : 정보 수정")
    public void 정보수정() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/me";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setEmail("user1Email@naver.com");
        updateUserRequestDto.setNewNickname("newUser");
        updateUserRequestDto.setNewImageUrl("ppiiccttuurree");
        //when
        this.restTemplate.put(uri,updateUserRequestDto);
        //then
        User user1 = userRepository.findByEmail("user1Email@naver.com").get();
        assertThat(user1.getNickname()).isEqualTo("newUser");
        assertThat(user1.getImageUrl()).isEqualTo("ppiiccttuurree");
    }

    @Test
    @DisplayName("테스트 05 : 정보 수정, 없는 이메일")
    public void 정보수정_없는이메일() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/me";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setEmail("user100000Email");
        updateUserRequestDto.setNewNickname("user123");
        updateUserRequestDto.setNewImageUrl("ppiiccttuurree");
        //when
        this.restTemplate.put(uri,updateUserRequestDto);
        //then
        Optional<User> userOption = userRepository.findByEmail("user100000Email");
        assertThat(userOption.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테스트 06 : 내 정보 검색")
    public void 내정보검색() throws URISyntaxException, JsonProcessingException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/me?email=user1Email@naver.com";
        URI uri = new URI(baseUrl);
        //when
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode root = mapper.readTree(result.getBody());
        assertThat(root.path("nickname").asText()).isEqualTo("user1");
        assertThat(root.path("imageUrl").asText()).isEqualTo("user1Picture");
    }
    @Test
    @DisplayName("테스트 07 : 이메일 형식 맞지 않음")
    public void 내정보검색_형식오류_없는이메일() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/member/me?email=user11231231Email";
        URI uri = new URI(baseUrl);
        //when
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
        //then
        //형식 오류는 BAD_REQUEST지만 create에서 체크하는거고, 이후는 해당 이메일이 없음.
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("테스트 08 : 로그인")
    public void 로그인() throws URISyntaxException, JsonProcessingException {
        //given
        final String baseUrl = "http://localhost:" + port + "/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("user1Email@naver.com");
        loginRequestDto.setPassword("123456");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        JsonNode root = mapper.readTree(result.getBody());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(root.path("email").asText()).isEqualTo("user1Email@naver.com");
    }
    @Test
    @DisplayName("테스트 09 : 로그인 실패, 해당 이메일 없음")
    public void 로그인_없는이메일() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("user123Email@naver.com");
        loginRequestDto.setPassword("123456");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("테스트 10 : 로그인 실패, 비밀번호 틀림")
    public void 로그인_실패() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("user1Email@naver.com");
        loginRequestDto.setPassword("1234565");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

