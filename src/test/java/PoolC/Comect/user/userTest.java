package PoolC.Comect.user;

import PoolC.Comect.user.dto.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

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
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void before() throws Exception {
        mongoTemplate.remove(new Query(),"data");
        mongoTemplate.remove(new Query(),"relation");
        mongoTemplate.remove(new Query(),"user");
        userTestDataLoader userTestDataLoader = new userTestDataLoader();
        userTestDataLoader.run();
    }


    @Test
    @DisplayName("테스트 01 : 유저 생성")
    public void createUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user4Email@naver.com");
        createUserRequestDto.setUserNickname("user4");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user4picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("테스트 02 : 유저 생성 시 같은 이메일 실패")
    public void createUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user1Email@naver.com");
        createUserRequestDto.setUserNickname("user3");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user3picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 03 : 유저 생성 시 이메일 형식 실패")
    public void createUser3() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user1Email");
        createUserRequestDto.setUserNickname("user3");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user3picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 04 : 정보 수정")
    public void updateUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/myInfoChange";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setUserEmail("user1Email@naver.com");
        updateUserRequestDto.setNewNickname("user123");
        updateUserRequestDto.setNewProfilePicture("ppiiccttuurree");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,updateUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 05 : 정보 수정, 없는 이메일")
    public void updateUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/myInfoChange";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setUserEmail("user100000Email");
        updateUserRequestDto.setNewNickname("user123");
        updateUserRequestDto.setNewProfilePicture("ppiiccttuurree");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,updateUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 06 : 내 정보 검색")
    public void findUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/myInfo";
        URI uri = new URI(baseUrl);
        ReadUserRequestDto readUserRequestDto = new ReadUserRequestDto();
        readUserRequestDto.setUserEmail("user1Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readUserRequestDto, String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("테스트 07 : 이메일 형식 맞지 않음")
    public void findUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/myInfo";
        URI uri = new URI(baseUrl);
        ReadUserRequestDto readUserRequestDto = new ReadUserRequestDto();
        readUserRequestDto.setUserEmail("user11231231Email");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readUserRequestDto, String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    @DisplayName("테스트 08 : 로그인")
    public void login1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUserEmail("user1Email@naver.com");
        loginRequestDto.setUserPassword("1234");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("테스트 09 : 로그인 실패, 해당 이메일 없음")
    public void login2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUserEmail("user123Email@naver.com");
        loginRequestDto.setUserPassword("1234");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    @DisplayName("테스트 10 : 로그인 실패, 비밀번호 틀림")
    public void login3() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/login";
        URI uri = new URI(baseUrl);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUserEmail("user1Email@naver.com");
        loginRequestDto.setUserPassword("12345");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,loginRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
