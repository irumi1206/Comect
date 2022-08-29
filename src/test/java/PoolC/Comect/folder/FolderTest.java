package PoolC.Comect.folder;

import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.dto.*;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
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
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class FolderTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    UserRepository userRepository;

    ObjectId idUpdate;
    ObjectMapper jsonMapper=new ObjectMapper();

    @Before
    public void before() {
        userRepository.deleteAll();
        folderRepository.deleteAll();

        Folder userRootFolder = new Folder("");
        User user1 = new User("user1", "user1Email@email.com", userRootFolder.get_id(), "user1Picture", "user1");
        folderRepository.save(userRootFolder);
        userRepository.save(user1);

        Folder folder11=new Folder("folder11");
        folderRepository.folderCreate(userRootFolder.get_id(),"",folder11);
        Folder folder12=new Folder("folder12");
        folderRepository.folderCreate(userRootFolder.get_id(),"",folder12);
        Link link11=new Link("link11","imageUrl","url",null,"true");
        folderRepository.linkCreate(userRootFolder.get_id(),"",link11);
        Link link12=new Link("link12","imageUrl","url",null,"true");
        folderRepository.linkCreate(userRootFolder.get_id(),"",link12);

        Folder folder21=new Folder("folder21");
        folderRepository.folderCreate(userRootFolder.get_id(),"folder11",folder21);
        Folder folder22=new Folder("folder22");
        folderRepository.folderCreate(userRootFolder.get_id(),"folder11",folder22);
        Link link21=new Link("link21","imageUrl","url",null,"true");
        folderRepository.linkCreate(userRootFolder.get_id(),"folder11",link21);
        Link link22=new Link("link22","imageUrl","url",null,"true");
        folderRepository.linkCreate(userRootFolder.get_id(),"folder11",link22);


        Folder folder31=new Folder("folder31");
        folderRepository.folderCreate(userRootFolder.get_id(),"folder11/folder21",folder31);
        Folder folder32=new Folder("folder32");
        folderRepository.folderCreate(userRootFolder.get_id(),"folder11/folder21",folder32);
        Link link31=new Link("link31","imageUrl","url",null,"true");
        idUpdate=link31.get_id();
        folderRepository.linkCreate(userRootFolder.get_id(),"folder11/folder21",link31);
        Link link32=new Link("link32","imageUrl","url",null,"true");
        folderRepository.linkCreate(userRootFolder.get_id(),"folder11/folder21",link32);


    }

    @Test
    @DisplayName("폴더 생성 성공1, 루트폴더")
    public void 폴더생성성공1_루트폴더() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user1Email@email.com";
        String path="";
        String name="folder13";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder13")).isEqualTo(true);

    }

    @Test
    @DisplayName("폴더 생성 성공2, 루트폴더 아닌 폴더")
    public void 폴더생성성공2_루트폴더아닌폴더() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user1Email@email.com";
        String path="folder11/folder21";
        String name="folder33";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder33")).isEqualTo(true);

    }

    @Test
    @DisplayName("폴더 생성 실패1, 이메일 형식 오류")
    public void 폴더생성실패1_이메일형식오류() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user1";
        String path="";
        String name="folder13";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 생성 실패2, 회원이 존재하지 않음")
    public void 폴더생성실패2_회원존재하지않음() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user2Email@email.com";
        String path="";
        String name="folder13";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 생성 실패3, 경로가 유효하지 않음")
    public void 폴더생성실패3_경로유효하지않음() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user1Email@email.com";
        String path="folder4";
        String name="folder13";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 생성 실패4, 이미 같은 이름의 파일이 존재함")
    public void 폴더생성실패4_같은이름의폴더존재() throws URISyntaxException{

        //given
        String baseUrl="http://localhost:" + port + "/folder";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String email="user1Email@email.com";
        String path="folder11/folder21";
        String name="folder31";
        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
                .email((email))
                .path(path)
                .name(name)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }

    @Test
    @DisplayName("폴더 조회 성공1, 루트 폴터")
    public void 폴더조회성공1_루트폴더() throws URISyntaxException, JsonProcessingException {

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="";
        String showLink="true";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder11");
        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder12");
        Assertions.assertThat(jsonBody.get("linkInfos").get(0).get("name").asText()).isEqualTo("link11");
        Assertions.assertThat(jsonBody.get("linkInfos").get(1).get("name").asText()).isEqualTo("link12");

    }

    @Test
    @DisplayName("폴더 조회 성공2, 루트 폴터 아닌 폴더")
    public void 폴더조회성공2_루트폴더아닌폴더() throws URISyntaxException, JsonProcessingException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="folder11/folder21";
        String showLink="true";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder31");
        Assertions.assertThat(jsonBody.get("linkInfos").get(0).get("name").asText()).isEqualTo("link31");
        Assertions.assertThat(jsonBody.get("linkInfos").get(1).get("name").asText()).isEqualTo("link32");

    }

    @Test
    @DisplayName("폴더 조회 실패1, 이메일 형식이 맞지 않음")
    public void 폴더조회실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email";
        String path="";
        String showLink="true";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 조회 실패2, 이메일존재하지않음")
    public void 폴더조회실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user2Email@email.com";
        String path="";
        String showLink="true";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 조회 실패3, 유효하지않는경로")
    public void 폴더조회실패3_유효하지않는경로() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="folder14/folder13";
        String showLink="true";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더폴더 조회 성공1, 루트 폴터")
    public void 폴더폴더조회성공1_루트폴더() throws URISyntaxException, JsonProcessingException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="";
        String showLink="false";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder11");
        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder12");
        Assertions.assertThat(jsonBody.get("linkInfos").size()).isEqualTo(0);

    }

    @Test
    @DisplayName("폴더폴더 조회 성공2, 루트 폴터 아닌 폴더")
    public void 폴더폴더조회성공2_루트폴더아닌폴더() throws URISyntaxException, JsonProcessingException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="folder11/folder21";
        String showLink="false";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder31");
        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder32");
        Assertions.assertThat(jsonBody.get("linkInfos").size()).isEqualTo(0);

    }

    @Test
    @DisplayName("폴더폴더 조회 실패1, 이메일 형식이 맞지 않음")
    public void 폴더폴더조회실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email";
        String path="";
        String showLink="false";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더폴더 조회 실패2, 이메일존재하지않음")
    public void 폴더폴더조회실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user2Email@email.com";
        String path="";
        String showLink="false";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더폴더 조회 실패3, 유효하지않는경로")
    public void 폴더폴더조회실패3_유효하지않는경로() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder";
        String email="user1Email@email.com";
        String path="folder14/folder13";
        String showLink="false";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path)
                .queryParam("showLink",showLink);


        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 수정 성공1, 루트폴더아닌 폴더")
    public void 폴더수정성공1_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="folder11/folder21";
        String newName="folder21Modified";

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .email(email)
                .path(path)
                .newName(newName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21Modified")).isEqualTo(true);

    }

    @Test
    @DisplayName("폴더 수정 실패1, 루트폴더")
    public void 폴더수정실패1_루트() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="";
        String newName="rootFolderModified";

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .email(email)
                .path(path)
                .newName(newName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 수정 실패2, 이메일형식오류")
    public void 폴더수정실패2_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@";
        String path="folder11/folder21";
        String newName="folder21Modified";

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .email(email)
                .path(path)
                .newName(newName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 수정 실패3, 존재하지않는이메일")
    public void 폴더수정실패3_존재하지않는이메일() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user2Email@email.com";
        String path="folder11/folder21";
        String newName="folder21Modified";

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .email(email)
                .path(path)
                .newName(newName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 수정 실패4, 경로유효하지않음")
    public void 폴더수정실패4_경로유효하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="folder11/folder23";
        String newName="folder21Modified";

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
                .email(email)
                .path(path)
                .newName(newName)
                .build();

        //when
        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 삭제 성공1, 루트폴더아닌폴더")
    public void 폴더삭제성공1_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11/folder21");

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21")).isEqualTo(false);

    }

    @Test
    @DisplayName("폴더 삭제 실패1, 루트폴더")
    public void 폴더삭제실패1_루트폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("");

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 삭제 실패2, 이메일형식오류")
    public void 폴더삭제실패2_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email";
        List<String> paths=new ArrayList<>();
        paths.add("folder11");

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 삭제 실패3, 이메일존재하지않음")
    public void 폴더삭제실패3_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user2Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11");

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 삭제 실패4, 경로유효하지않음")
    public void 폴더삭제실패4_경로유효하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11/folder24");

        String baseUrl="http://localhost:" + port + "/folder";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("폴더 이동 성공1, 루트폴더로")
    public void 폴더이동성공1_루트폴더로() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        String modifiedPath="";

        String baseUrl="http://localhost:" + port + "/folder/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder21")).isEqualTo(true);

    }

    @Test
    @DisplayName("폴더 이동 성공2, 루트폴더아닌폴더")
    public void 폴더이동성공2_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder12");
        String modifiedPath="folder11/folder21/folder31";

        String baseUrl="http://localhost:" + port + "/folder/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31/folder12")).isEqualTo(true);

    }

    @Test
    @DisplayName("폴더 이동 실패1, 이메일형식오류")
    public void 폴더이동실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        String modifiedPath="";

        String baseUrl="http://localhost:" + port + "/folder/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 이동 실패2, 이메일존재하지않음")
    public void 폴더이동실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user2Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        String modifiedPath="";

        String baseUrl="http://localhost:" + port + "/folder/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 이동 실패3, 경로유효하지않음")
    public void 폴더이동실패3_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add(" folder11/folder21");
        String modifiedPath="folder11/folder21/folder31";

        String baseUrl="http://localhost:" + port + "/folder/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21")).isEqualTo(true);
        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder21/folder21")).isEqualTo(false);

    }

    @Test
    @DisplayName("폴더 경로 유효성 성공1, 루트폴더")
    public void 폴더경로유효성성공1_루트폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder/path";
        String email="user1Email@email.com";
        String path="";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("폴더 경로 유효성 성공2, 루트폴더 아닌 유효한폴더")
    public void 폴더경로유효성성공2_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder/path";
        String email="user1Email@email.com";
        String path="folder11/folder21/folder31";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("폴더 경로 유효성 성공3, 루트폴더 아닌 유효하지 않은폴더")
    public void 폴더경로유효성성공3_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder/path";
        String email="user1Email@email.com";
        String path="folder11/folder21/folder33";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("폴더 경로 유효성 실패1, 이메일형식오류")
    public void 폴더경로유효성실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder/path";
        String email="user1Email";
        String path="folder11/folder21/folder31";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("폴더 경로 유효성 실패2, 이메일존재하지않음")
    public void 폴더경로유효성실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");

        String baseUrl="http://localhost:" + port + "/folder/path";
        String email="user3Email@email.com";
        String path="folder11/folder21/folder31";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("email", email)
                .queryParam("path",path);

        //when
        HttpEntity<Void> request=new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("링크 생성성공1, 루트폴더에 생성")
    public void 링크생성성공1_루트폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="";
        String name="link13";
        String url="url";
        String imageUrl="imageUrl";
        List<String> keywords=new ArrayList<>();
        keywords.add("keyword1,keyword2");
        String isPublic="true";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"link13")).isEqualTo(true);
    }

    @Test
    @DisplayName("링크 생성성공2, 루트폴더아닌폴더")
    public void 링크생성성공2_루트폴더아닌폴더() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="folder11/folder21/folder31";
        String name="link41";
        String url="url";
        String imageUrl="imageUrl";
        List<String> keywords=new ArrayList<>();
        keywords.add("keyword1,keyword2");
        String isPublic="true";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31/link41")).isEqualTo(true);
    }

    @Test
    @DisplayName("링크 생성실패1, 이메일형식오류")
    public void 링크생성실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email";
        String path="folder11/folder21/folder31";
        String name="link41";
        String url="url";
        String imageUrl="imageUrl";
        List<String> keywords=new ArrayList<>();
        keywords.add("keyword1,keyword2");
        String isPublic="true";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크 생성실패2, 이메일존재하지않음")
    public void 링크생성실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user2Email@email.com";
        String path="folder11/folder21/folder31";
        String name="link41";
        String url="url";
        String imageUrl="imageUrl";
        List<String> keywords=new ArrayList<>();
        keywords.add("keyword1,keyword2");
        String isPublic="true";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크 생성실패3, 경로유효성오류")
    public void 링크생성실패3_경로유효성오() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String email="user1Email@email.com";
        String path="folder11/folder21/folder33";
        String name="link41";
        String url="url";
        String imageUrl="imageUrl";
        List<String> keywords=new ArrayList<>();
        keywords.add("keyword1,keyword2");
        String isPublic="true";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("링크수정성공1")
    public void 링크수정성공1() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        String path="folder11/folder21";
        String name="folder31Modified";
        String url="newUrl";
        String imageUrl="newImageUrl";
        List<String> keywords=new ArrayList<>();
        String isPublic="false";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
                .id(id)
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31Modified")).isEqualTo(true);
    }

    @Test
    @DisplayName("링크수정실패1, 이메일형식오류")
    public void 링크수정실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Emaim";
        String path="folder11/folder21";
        String name="folder31Modified";
        String url="newUrl";
        String imageUrl="newImageUrl";
        List<String> keywords=new ArrayList<>();
        String isPublic="false";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
                .id(id)
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크수정실패2, 이메일존재하지않음")
    public void 링크수정실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user2Email@email.com";
        String path="folder11/folder21";
        String name="folder31Modified";
        String url="newUrl";
        String imageUrl="newImageUrl";
        List<String> keywords=new ArrayList<>();
        String isPublic="false";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
                .id(id)
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크수정실패3, 유효하지않는경로")
    public void 링크수정실패3_유효하지않는경로() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        String path="folder11/folder23";
        String name="folder31Modified";
        String url="newUrl";
        String imageUrl="newImageUrl";
        List<String> keywords=new ArrayList<>();
        String isPublic="false";

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
                .id(id)
                .email(email)
                .path(path)
                .name(name)
                .url(url)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .isPublic(isPublic)
                .build();

        //when
        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("링크삭제성공1")
    public void 링크삭제성공1() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11/folder21");
        List<String> ids=new ArrayList<>();
        ids.add(id);

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
                .ids(ids)
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/link31")).isEqualTo(false);
    }

    @Test
    @DisplayName("링크삭제실패1, 이메일 형식오류")
    public void 링크삭제실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email";
        List<String> paths=new ArrayList<>();
        paths.add("folder11/folder21");
        List<String> ids=new ArrayList<>();
        ids.add(id);

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
                .ids(ids)
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크삭제실패2, 이메일존재하지않음")
    public void 링크삭제실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user2Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11/folder21");
        List<String> ids=new ArrayList<>();
        ids.add(id);

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
                .ids(ids)
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크삭제실패3, 경로유효하지않음")
    public void 링크삭제실패3_경로유효하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        List<String> paths=new ArrayList<>();
        paths.add("folder11");
        List<String> ids=new ArrayList<>();
        ids.add(id);

        String baseUrl="http://localhost:" + port + "/link";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
                .ids(ids)
                .email(email)
                .paths(paths)
                .build();

        //when
        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("링크이동성공1")
    public void 링크이동성공1() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        List<String> originalIds=new ArrayList<>();
        originalIds.add(id);
        String modifiedPath="folder11/folder22";

        String baseUrl="http://localhost:" + port + "/link/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .originalIds(originalIds)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder22/link31")).isEqualTo(true);
    }

    @Test
    @DisplayName("링크이동실패1, 이메일 형식 오류")
    public void 링크이동실패1_이메일형식오류() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        List<String> originalIds=new ArrayList<>();
        originalIds.add(id);
        String modifiedPath="folder11/folder22";

        String baseUrl="http://localhost:" + port + "/link/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .originalIds(originalIds)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크이동실패2, 이메일존재하지않음")
    public void 링크이동실패2_이메일존재하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user2Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        List<String> originalIds=new ArrayList<>();
        originalIds.add(id);
        String modifiedPath="folder11/folder22";

        String baseUrl="http://localhost:" + port + "/link/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .originalIds(originalIds)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("링크이동실패3, 경로유효하지않음")
    public void 링크이동실패3_경로유효하지않음() throws URISyntaxException{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String id=idUpdate.toString();
        String email="user1Email@email.com";
        List<String> originalPaths=new ArrayList<>();
        originalPaths.add("folder11/folder21");
        List<String> originalIds=new ArrayList<>();
        originalIds.add(id);
        String modifiedPath="folder11/folder24";

        String baseUrl="http://localhost:" + port + "/link/path";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
                .email(email)
                .originalPaths(originalPaths)
                .originalIds(originalIds)
                .modifiedPath(modifiedPath)
                .build();

        //when
        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                String.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }










}
