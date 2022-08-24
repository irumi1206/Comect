package PoolC.Comect.data;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.dto.*;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DataTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    DataRepository dataRepository;
    @Autowired
    UserRepository userRepository;

    @Before
    public void before() {
        userRepository.deleteAll();
        dataRepository.deleteAll();

        Data user1Data = new Data();
        User user1 = new User("user1", "user1Email@email.com", user1Data.getId(), "user1Picture", "user1");
        dataRepository.save(user1Data);
        userRepository.save(user1);

        Data user2Data = new Data();
        User user2 = new User("user2", "user2Email@email.com", user2Data.getId(), "user2Picture", "user2");
        dataRepository.save(user2Data);
        userRepository.save(user2);

        Data user3Data = new Data();
        User user3 = new User("user3", "user3Email@email.com", user3Data.getId(), "user3Picture", "user3");
        dataRepository.save(user3Data);
        userRepository.save(user3);

        Folder folder1=new Folder("folder1");
        dataRepository.folderCreate(user1Data.getId(),"",folder1);

        Folder folder2=new Folder("folder2");
        dataRepository.folderCreate(user1Data.getId(),"",folder2);

        Folder folder3=new Folder("folder3");
        dataRepository.folderCreate(user1Data.getId(),"folder1",folder3);

        Folder folder4=new Folder("folder4");
        dataRepository.folderCreate(user1Data.getId(),"folder1",folder4);

    }

    @Test
    @DisplayName("테스트 01 : 폴더 생성 성공")
    public void 폴더생성성공() throws URISyntaxException {

        //given
        String baseUrl="http://localhost:" + port + "/folder/create";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="";
        String folderName="folder3";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .userEmail((userEmail))
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("테스트 02 : 폴더 생성 실패, 이메일에 해당하는 회원이 없음")
    public void 폴더생성실패1() throws URISyntaxException {

        //given
        String baseUrl="http://localhost:" + port + "/folder/create";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String path="";
        String folderName="folder3";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .userEmail((userEmail))
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 03 : 폴더 생성 실패, 이메일 형식이 맞지 않음")
    public void 폴더생성실패2() throws URISyntaxException {

        //given
        String baseUrl="http://localhost:" + port + "/folder/create";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email";
        String path="";
        String folderName="folder3";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .userEmail((userEmail))
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("테스트 04 : 폴더 생성 실패, 경로가 유효하지 않음")
    public void 폴더생성실패3() throws URISyntaxException {

        //given
        String baseUrl="http://localhost:" + port + "/folder/create";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="/folder4";
        String folderName="folder3";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .userEmail((userEmail))
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 05 : 폴더 조회 성공 (루트폴더 조회)")
    public void 폴더조회성공1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/read";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="";
        FolderReadRequestDto folderReadRequestDto= FolderReadRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        //Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 06 : 폴더 조회 성공 (루트폴더 아닌 폴더 조회)")
    public void 폴더조회성공2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/read";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder1";
        FolderReadRequestDto folderReadRequestDto= FolderReadRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 07 : 폴더 조회 실패, 이메일에 해당하는 회원이 없음")
    public void 폴더조회실패1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/read";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String path="folder1";
        FolderReadRequestDto folderReadRequestDto= FolderReadRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 08 : 폴더 조회 실패, 이메일 형식이 맞지 않음")
    public void 폴더조회실패2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/read";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1";
        String path="folder1";
        FolderReadRequestDto folderReadRequestDto= FolderReadRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 09 : 폴더 조회 실패, 유효하지 않은 경로")
    public void 폴더조회실패3() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/read";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder3";
        FolderReadRequestDto folderReadRequestDto= FolderReadRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 10 : 폴더폴더 조회 성공 (루트폴더 조회)")
    public void 폴더폴더조회성공1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/readFolder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="";
        FolderReadFolderRequestDto folderReadRequestDto= FolderReadFolderRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadFolderRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 11 : 폴더폴더 조회 성공 (루트폴더 아닌 폴더)")
    public void 폴더폴더조회성공2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/readFolder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder1";
        FolderReadFolderRequestDto folderReadRequestDto= FolderReadFolderRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadFolderRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(uri,request, Void.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 12 : 폴더폴더 조회 실패, 이메일에 해당하는 회원이 없음")
    public void 폴더폴더조회실패1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/readFolder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String path="";
        FolderReadFolderRequestDto folderReadRequestDto= FolderReadFolderRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadFolderRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 13 : 폴더폴더 조회 실패, 이메일 형식이 맞지 않음")
    public void 폴더폴더조회실패2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/readFolder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1";
        String path="";
        FolderReadFolderRequestDto folderReadRequestDto= FolderReadFolderRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadFolderRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 14 : 폴더폴더 조회 실패, 경로가 유효하지 않음")
    public void 폴더폴더조회실패3() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/readFolder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder4";
        FolderReadFolderRequestDto folderReadRequestDto= FolderReadFolderRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderReadFolderRequestDto> request = new HttpEntity<>(folderReadRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(result.getBody());
    }

    @Test
    @DisplayName("테스트 15 : 폴더 수정 성공")
    public void 폴더수정성공() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/update";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder1";
        String folderName="newFolder1";
        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("테스트 16 : 폴더 수정 실패, 이메일에 해당하는 회원이 없음")
    public void 폴더수정실패1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/update";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String path="folder1";
        String folderName="newFolder1";
        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 17 : 폴더 수정 실패, 이메일 형식이 맞지 않음")
    public void 폴더수정실패2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/update";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1";
        String path="folder1";
        String folderName="newFolder1";
        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("테스트 18 : 폴더 수정 실패, 경로가 유효하지 않음")
    public void 폴더수정실패3() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/update";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder4";
        String folderName="newFolder4";
        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .folderName(folderName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 19 : 폴더 삭제 성공(루트폴더가 아닌 폴더)")
    public void 폴더삭제성공1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/delete";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder1";
        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("테스트 20 : 폴더 삭제 실패, 루트 파일 삭제 시도")
    public void 폴더삭제실패1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/delete";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="";
        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 21 : 폴더 삭제 실패, 이메일에 해당하는 유저가 없음")
    public void 폴더삭제실패2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/delete";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String path="folder1";
        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 22 : 폴더 삭제 실패, 이메일 형식이 잘못됨")
    public void 폴더삭제실패3() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/delete";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1";
        String path="folder1";
        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("테스트 23 : 폴더 삭제 실패, 유효하지 않은 경로")
    public void 폴더삭제실패4() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/delete";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String path="folder3";
        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .userEmail(userEmail)
                .path(path)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 24 : 폴더 이동 성공")
    public void 폴더이동성공() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/move";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String originalPath="folder1/folder4";
        String modifiedPath="folder2";
        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .userEmail(userEmail)
                .originalPath(originalPath)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("테스트 24 : 폴더 이동 실패, 이메일에 해당하는 회원이 없음")
    public void 폴더이동실패1() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/move";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user4Email@email.com";
        String originalPath="folder1/folder4";
        String modifiedPath="folder2";
        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .userEmail(userEmail)
                .originalPath(originalPath)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 24 : 폴더 이동 실패, 이메일 형식이 맞지 않음")
    public void 폴더이동실패2() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/move";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email";
        String originalPath="folder1/folder4";
        String modifiedPath="folder2";
        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .userEmail(userEmail)
                .originalPath(originalPath)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("테스트 24 : 폴더 이동 실패, 유효하지 않은 본래 경로")
    public void 폴더이동실패3() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/move";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String originalPath="folder4";
        String modifiedPath="folder2";
        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .userEmail(userEmail)
                .originalPath(originalPath)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("테스트 24 : 폴더 이동 실패, 유효하지 않는 도착 경로")
    public void 폴더이동실패4() throws URISyntaxException{
        //given
        String baseUrl="http://localhost:" + port + "/folder/move";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String userEmail="user1Email@email.com";
        String originalPath="folder1/folder4";
        String modifiedPath="folder5";
        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .userEmail(userEmail)
                .originalPath(originalPath)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }



}
