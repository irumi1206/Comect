package PoolC.Comect.user;

import PoolC.Comect.user.dto.CreateUserRequestDto;
import PoolC.Comect.user.dto.ReadUserRequestDto;
import PoolC.Comect.user.dto.ReadUserResponseDto;
import PoolC.Comect.user.dto.UpdateUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("user 생성 테스트, 성공")
    void createUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user3Email");
        createUserRequestDto.setUserNickname("user3");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user3picture");

        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        //when
        HttpEntity<CreateUserRequestDto> request = new HttpEntity<>(createUserRequestDto,headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("user 생성 테스트, 같은 이메일 실패")
    void createUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user1Email");
        createUserRequestDto.setUserNickname("user3");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user3picture");

        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        //when
        HttpEntity<CreateUserRequestDto> request = new HttpEntity<>(createUserRequestDto,headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,request,String.class);
        //then
        assertThat(result.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("user 생성 테스트, 이메일 형식 실패")
    void createUser3() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setUserEmail("user1Email@@@");
        createUserRequestDto.setUserNickname("user3");
        createUserRequestDto.setUserPassword("1234");
        createUserRequestDto.setProfilePicture("user3picture");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("내 정보 수정, 성공")
    void updateUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setUserEmail("user1Email");
        updateUserRequestDto.setNewNickname("user123");
        updateUserRequestDto.setNewProfilePicture("ppiiccttuurree");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,updateUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("내 정보 수정, 실패 사람 존재하지 않음")
    void updateUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setUserEmail("user100000Email");
        updateUserRequestDto.setNewNickname("user123");
        updateUserRequestDto.setNewProfilePicture("ppiiccttuurree");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,updateUserRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("내 정보 조회, 성공")
    void findUser1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        ReadUserRequestDto readUserRequestDto = new ReadUserRequestDto();
        readUserRequestDto.setUserEmail("user1Email");
        //when
        ResponseEntity<ReadUserResponseDto> result = this.restTemplate.postForEntity(uri,readUserRequestDto, ReadUserResponseDto.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("내 정보 조회, 실패 해당 이메일 없음")
    void findUser2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/auth/signUp";
        URI uri = new URI(baseUrl);
        ReadUserRequestDto readUserRequestDto = new ReadUserRequestDto();
        readUserRequestDto.setUserEmail("user11231231Email");
        //when
        ResponseEntity<ReadUserResponseDto> result = this.restTemplate.postForEntity(uri,readUserRequestDto, ReadUserResponseDto.class);
        //then
        assertThat(result.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }
}