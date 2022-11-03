package PoolC.Comect.folder;

import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.EmailService;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ActiveProfiles("test")
public class FolderTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    ElasticUserRepository elasticUserRepository;
    @Autowired
    ElasticFolderRepository elasticFolderRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;

    public void deleteAllTestData(){
        userRepository.deleteAll();
        folderRepository.deleteAll();
        elasticUserRepository.deleteAll();
        elasticFolderRepository.deleteAll();
    }

    public void authorizedLogin1(){
        String email="user1@email.com";
        String password="user1Password";
        String nickname="user1Nickname";
        Boolean joinReturn=userService.join(email,password,nickname,null);
        String user1Id=emailRepository.findByEmail("user1@email.com").get().getId().toString();
        emailService.emailCheck(user1Id);

    }

    @Before
    public void before() {
        deleteAllTestData();
        authorizedLogin1();


    }

    @Test
    @DisplayName("1. 폴더생성")
    public void 폴더생성(){

    }
//
//    @Test
//    @DisplayName("폴더 생성 성공1, 루트폴더")
//    public void 폴더생성성공1_루트폴더() throws URISyntaxException{
//
//        //given
//        String baseUrl="http://localhost:" + port + "/folder";
//        URI uri =new URI(baseUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String email="user1Email@email.com";
//        String path="";
//        String name="folder13";
//        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
//                .email((email))
//                .path(path)
//                .name(name)
//                .build();
//
//        //when
//        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
//        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
//
//        //then
//        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder13")).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("폴더 생성 성공2, 루트폴더 아닌 폴더")
//    public void 폴더생성성공2_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        String baseUrl="http://localhost:" + port + "/folder";
//        URI uri =new URI(baseUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String name="folder33";
//        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
//                .email((email))
//                .path(path)
//                .name(name)
//                .build();
//
//        //when
//        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
//        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
//
//        //then
//        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder33")).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("폴더 생성 실패2, 회원이 존재하지 않음")
//    public void 폴더생성실패2_회원존재하지않음() throws URISyntaxException{
//
//        //given
//        String baseUrl="http://localhost:" + port + "/folder";
//        URI uri =new URI(baseUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String email="user2Email@email.com";
//        String path="";
//        String name="folder13";
//        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
//                .email((email))
//                .path(path)
//                .name(name)
//                .build();
//
//        //when
//        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
//        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
//
//        //then
//        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 생성 실패3, 경로가없음")
//    public void 폴더생성실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        String baseUrl="http://localhost:" + port + "/folder";
//        URI uri =new URI(baseUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String email="user1Email@email.com";
//        String path="folder4";
//        String name="folder13";
//        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
//                .email((email))
//                .path(path)
//                .name(name)
//                .build();
//
//        //when
//        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
//        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
//
//        //then
//        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 생성 실패4, 이미 같은 이름의 파일이 존재함")
//    public void 폴더생성실패4_같은이름의폴더존재() throws URISyntaxException{
//
//        //given
//        String baseUrl="http://localhost:" + port + "/folder";
//        URI uri =new URI(baseUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String name="folder31";
//        FolderCreateRequestDto folderCreateRequestDto= FolderCreateRequestDto.builder()
//                .email((email))
//                .path(path)
//                .name(name)
//                .build();
//
//        //when
//        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(folderCreateRequestDto,headers);
//        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);
//
//        //then
//        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
//
//    }
//
//    @Test
//    @DisplayName("폴더 조회 성공1, 루트 폴터")
//    public void 폴더조회성공1_루트폴더() throws URISyntaxException, JsonProcessingException {
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user1Email@email.com";
//        String path="";
//        String showLink="true";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
//        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder11");
//        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder12");
//        Assertions.assertThat(jsonBody.get("linkInfos").get(0).get("name").asText()).isEqualTo("link11");
//        Assertions.assertThat(jsonBody.get("linkInfos").get(1).get("name").asText()).isEqualTo("link12");
//
//    }
//
//    @Test
//    @DisplayName("폴더 조회 성공2, 루트 폴터 아닌 폴더")
//    public void 폴더조회성공2_루트폴더아닌폴더() throws URISyntaxException, JsonProcessingException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String showLink="true";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
//        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder31");
//        Assertions.assertThat(jsonBody.get("linkInfos").get(0).get("name").asText()).isEqualTo("link31");
//        Assertions.assertThat(jsonBody.get("linkInfos").get(1).get("name").asText()).isEqualTo("link32");
//
//    }
//
//    @Test
//    @DisplayName("폴더 조회 실패1, 이메일존재하지않음")
//    public void 폴더조회실패1_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user2Email@email.com";
//        String path="";
//        String showLink="true";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 조회 실패2, 경로가없음")
//    public void 폴더조회실패2_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user1Email@email.com";
//        String path="folder14/folder13";
//        String showLink="true";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더폴더 조회 성공1, 루트 폴터")
//    public void 폴더폴더조회성공1_루트폴더() throws URISyntaxException, JsonProcessingException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/me";
//        String email="user1Email@email.com";
//        String path="";
//        String showLink="false";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
//        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder11");
//        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder12");
//        Assertions.assertThat(jsonBody.get("linkInfos").size()).isEqualTo(0);
//
//    }
//
//    @Test
//    @DisplayName("폴더폴더 조회 성공2, 루트 폴터 아닌 폴더")
//    public void 폴더폴더조회성공2_루트폴더아닌폴더() throws URISyntaxException, JsonProcessingException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/me";
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String showLink="false";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        JsonNode jsonBody = jsonMapper.readTree(response.getBody());
//        Assertions.assertThat(jsonBody.get("folderInfos").get(0).get("name").asText()).isEqualTo("folder31");
//        Assertions.assertThat(jsonBody.get("folderInfos").get(1).get("name").asText()).isEqualTo("folder32");
//        Assertions.assertThat(jsonBody.get("linkInfos").size()).isEqualTo(0);
//
//    }
//
//    @Test
//    @DisplayName("폴더폴더 조회 실패1, 이메일존재하지않음")
//    public void 폴더폴더조회실패1_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user2Email@email.com";
//        String path="";
//        String showLink="false";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더폴더 조회 실패2, 경로가없음")
//    public void 폴더폴더조회실패2_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        String email="user1Email@email.com";
//        String path="folder14/folder13";
//        String showLink="false";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path)
//                .queryParam("showLink",showLink);
//
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 수정 성공1, 루트폴더아닌 폴더")
//    public void 폴더수정성공1_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String newName="new21";
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .newName(newName)
//                .build();
//
//        //when
//        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/new21")).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("폴더 수정 실패1, 루트폴더")
//    public void 폴더수정실패1_루트() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="";
//        String newName="newroot";
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .newName(newName)
//                .build();
//
//        //when
//        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//
//    }
//
//    @Test
//    @DisplayName("폴더 수정 실패2, 존재하지않는이메일")
//    public void 폴더수정실패2_존재하지않는이메일() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user2Email@email.com";
//        String path="folder11/folder21";
//        String newName="new21";
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .newName(newName)
//                .build();
//
//        //when
//        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 수정 실패3, 경로가없음")
//    public void 폴더수정실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="folder11/folder23";
//        String newName="new21";
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderUpdateRequestDto folderUpdateRequestDto= FolderUpdateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .newName(newName)
//                .build();
//
//        //when
//        HttpEntity<FolderUpdateRequestDto> request = new HttpEntity<>(folderUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 삭제 성공1, 루트폴더아닌폴더")
//    public void 폴더삭제성공1_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> paths=new ArrayList<>();
//        paths.add("folder11/folder21");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
//                .email(email)
//                .paths(paths)
//                .build();
//
//        //when
//        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21")).isEqualTo(false);
//
//    }
//
//    @Test
//    @DisplayName("폴더 삭제 실패1, 루트폴더")
//    public void 폴더삭제실패1_루트폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> paths=new ArrayList<>();
//        paths.add("");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
//                .email(email)
//                .paths(paths)
//                .build();
//
//        //when
//        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//
//    }
//
//    @Test
//    @DisplayName("폴더 삭제 실패3, 이메일존재하지않음")
//    public void 폴더삭제실패3_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user2Email@email.com";
//        List<String> paths=new ArrayList<>();
//        paths.add("folder11");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
//                .email(email)
//                .paths(paths)
//                .build();
//
//        //when
//        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 삭제 실패4, 경로가없음")
//    public void 폴더삭제실패4_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> paths=new ArrayList<>();
//        paths.add("folder11/folder24");
//
//        String baseUrl="http://localhost:" + port + "/folder";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderDeleteRequestDto folderDeleteRequestDto= FolderDeleteRequestDto.builder()
//                .email(email)
//                .paths(paths)
//                .build();
//
//        //when
//        HttpEntity<FolderDeleteRequestDto> request = new HttpEntity<>(folderDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 이동 성공1, 루트폴더로")
//    public void 폴더이동성공1_루트폴더로() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> originalPaths=new ArrayList<>();
//        originalPaths.add("folder11/folder21");
//        String modifiedPath="";
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
//                .email(email)
//                .originalPaths(originalPaths)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        //Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder21")).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("폴더 이동 성공2, 루트폴더아닌폴더")
//    public void 폴더이동성공2_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> originalPaths=new ArrayList<>();
//        originalPaths.add("folder12");
//        String modifiedPath="folder11/folder21/folder31";
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
//                .email(email)
//                .originalPaths(originalPaths)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31/folder12")).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("폴더 이동 실패2, 이메일존재하지않음")
//    public void 폴더이동실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user2Email@email.com";
//        List<String> originalPaths=new ArrayList<>();
//        originalPaths.add("folder11/folder21");
//        String modifiedPath="";
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
//                .email(email)
//                .originalPaths(originalPaths)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("폴더 이동 실패3, 경로가없음")
//    public void 폴더이동실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        List<String> originalPaths=new ArrayList<>();
//        originalPaths.add(" folder11/folder21");
//        String modifiedPath="folder11/folder21/folder31";
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        FolderMoveRequestDto folderMoveRequestDto= FolderMoveRequestDto.builder()
//                .email(email)
//                .originalPaths(originalPaths)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<FolderMoveRequestDto> request = new HttpEntity<>(folderMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21")).isEqualTo(true);
//        Assertions.assertThat(folderRepository.checkPathFolder(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder21/folder21")).isEqualTo(false);
//
//    }
//
//    @Test
//    @DisplayName("폴더 경로 유효성 성공1, 루트폴더")
//    public void 폴더경로유효성성공1_루트폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        String email="user1Email@email.com";
//        String path="";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    }
//
//    @Test
//    @DisplayName("폴더 경로 유효성 성공2, 루트폴더 아닌 유효한폴더")
//    public void 폴더경로유효성성공2_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        String email="user1Email@email.com";
//        String path="folder11/folder21/folder31";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    }
//
//    @Test
//    @DisplayName("폴더 경로 유효성 성공3, 루트폴더 아닌 유효하지 않은폴더")
//    public void 폴더경로유효성성공3_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        String email="user1Email@email.com";
//        String path="folder11/folder21/folder33";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    }
//
//    @Test
//    @DisplayName("폴더 경로 유효성 실패2, 이메일존재하지않음")
//    public void 폴더경로유효성실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//
//        String baseUrl="http://localhost:" + port + "/folder/path";
//        String email="user3Email@email.com";
//        String path="folder11/folder21/folder31";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("email", email)
//                .queryParam("path",path);
//
//        //when
//        HttpEntity<Void> request=new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("링크 생성성공1, 루트폴더에 생성")
//    public void 링크생성성공1_루트폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="";
//        String name="link13";
//        String url="url";
//        String imageUrl="imageUrl";
//        List<String> keywords=new ArrayList<>();
//        keywords.add("keyword1,keyword2");
//        String isPublic="true";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .build();
//
//        //when
//        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.POST,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"link13")).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("링크 생성성공2, 루트폴더아닌폴더")
//    public void 링크생성성공2_루트폴더아닌폴더() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="folder11/folder21/folder31";
//        String name="link41";
//        String url="url";
//        String imageUrl="imageUrl";
//        List<String> keywords=new ArrayList<>();
//        keywords.add("keyword1,keyword2");
//        String isPublic="true";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .build();
//
//        //when
//        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.POST,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31/link41")).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("링크 생성실패2, 이메일존재하지않음")
//    public void 링크생성실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user2Email@email.com";
//        String path="folder11/folder21/folder31";
//        String name="link41";
//        String url="url";
//        String imageUrl="imageUrl";
//        List<String> keywords=new ArrayList<>();
//        keywords.add("keyword1,keyword2");
//        String isPublic="true";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .build();
//
//        //when
//        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.POST,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크 생성실패3, 경로가없음")
//    public void 링크생성실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String email="user1Email@email.com";
//        String path="folder11/folder21/folder33";
//        String name="link41";
//        String url="url";
//        String imageUrl="imageUrl";
//        List<String> keywords=new ArrayList<>();
//        keywords.add("keyword1,keyword2");
//        String isPublic="true";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkCreateRequestDto linkCreateRequestDto= LinkCreateRequestDto.builder()
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .build();
//
//        //when
//        HttpEntity<LinkCreateRequestDto> request = new HttpEntity<>(linkCreateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.POST,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크수정성공1")
//    public void 링크수정성공1() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        String name="folder31Modified";
//        String url="newUrl";
//        String imageUrl="newImageUrl";
//        List<String> keywords=new ArrayList<>();
//        String isPublic="false";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
//                .id(id)
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .imageChange("false")
//                .build();
//
//        //when
//        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/folder31Modified")).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("링크수정실패2, 이메일존재하지않음")
//    public void 링크수정실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user2Email@email.com";
//        String path="folder11/folder21";
//        String name="folder31Modified";
//        String url="newUrl";
//        String imageUrl="newImageUrl";
//        List<String> keywords=new ArrayList<>();
//        String isPublic="false";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
//                .id(id)
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .imageChange("false")
//                .build();
//
//        //when
//        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크수정실패3, 경로가없음")
//    public void 링크수정실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String path="folder11/folder23";
//        String name="folder31Modified";
//        String url="newUrl";
//        String imageUrl="newImageUrl";
//        List<String> keywords=new ArrayList<>();
//        String isPublic="false";
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkUpdateRequestDto linkUpdateRequestDto= LinkUpdateRequestDto.builder()
//                .id(id)
//                .email(email)
//                .path(path)
//                .name(name)
//                .url(url)
//                .multipartFile(null)
//                .keywords(keywords)
//                .isPublic(isPublic)
//                .imageChange("false")
//                .build();
//
//        //when
//        HttpEntity<LinkUpdateRequestDto> request = new HttpEntity<>(linkUpdateRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크삭제성공1")
//    public void 링크삭제성공1() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String path="folder11/folder21";
//        List<String> ids=new ArrayList<>();
//        ids.add(id);
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
//                .ids(ids)
//                .email(email)
//                .path(path)
//                .build();
//
//        //when
//        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder21/link31")).isEqualTo(false);
//    }
//
//    @Test
//    @DisplayName("링크삭제실패2, 이메일존재하지않음")
//    public void 링크삭제실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user2Email@email.com";
//        String path="folder11/folder21";
//        List<String> ids=new ArrayList<>();
//        ids.add(id);
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
//                .ids(ids)
//                .email(email)
//                .path(path)
//                .build();
//
//        //when
//        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크삭제실패3, 경로가없음")
//    public void 링크삭제실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String path="folder11";
//        List<String> ids=new ArrayList<>();
//        ids.add(id);
//
//        String baseUrl="http://localhost:" + port + "/link";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkDeleteRequestDto linkDeleteRequestDto= LinkDeleteRequestDto.builder()
//                .ids(ids)
//                .email(email)
//                .path(path)
//                .build();
//
//        //when
//        HttpEntity<LinkDeleteRequestDto> request = new HttpEntity<>(linkDeleteRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.DELETE,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크이동성공1")
//    public void 링크이동성공1() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String originalPath="folder11/folder21";
//        List<String> originalIds=new ArrayList<>();
//        originalIds.add(id);
//        String modifiedPath="folder11/folder22";
//
//        String baseUrl="http://localhost:" + port + "/link/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
//                .email(email)
//                .originalPath(originalPath)
//                .originalIds(originalIds)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(folderRepository.checkPathLink(userRepository.findByEmail(email).get().getRootFolderId(),"folder11/folder22/link31")).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("링크이동실패2, 이메일존재하지않음")
//    public void 링크이동실패2_이메일존재하지않음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user2Email@email.com";
//        String originalPath="folder11/folder21";
//        List<String> originalIds=new ArrayList<>();
//        originalIds.add(id);
//        String modifiedPath="folder11/folder22";
//
//        String baseUrl="http://localhost:" + port + "/link/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
//                .email(email)
//                .originalPath(originalPath)
//                .originalIds(originalIds)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("링크이동실패3, 경로가없음")
//    public void 링크이동실패3_경로가없음() throws URISyntaxException{
//
//        //given
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("JSON","true");
//        String id=idUpdate.toString();
//        String email="user1Email@email.com";
//        String originalPath="folder11/folder21";
//        List<String> originalIds=new ArrayList<>();
//        originalIds.add(id);
//        String modifiedPath="folder11/folder24";
//
//        String baseUrl="http://localhost:" + port + "/link/path";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        LinkMoveRequestDto linkMoveRequestDto= LinkMoveRequestDto.builder()
//                .email(email)
//                .originalPath(originalPath)
//                .originalIds(originalIds)
//                .modifiedPath(modifiedPath)
//                .build();
//
//        //when
//        HttpEntity<LinkMoveRequestDto> request = new HttpEntity<>(linkMoveRequestDto,headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.PUT,
//                request,
//                String.class);
//
//        //then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//
//
//
//
//
//
//
//
//
}
