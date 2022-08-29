package PoolC.Comect.relation;

import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.relation.domain.Relation;
import PoolC.Comect.relation.domain.RelationType;
import PoolC.Comect.relation.dto.*;
import PoolC.Comect.relation.repository.RelationRepository;
import PoolC.Comect.user.domain.User;
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
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class relationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();


    @Before
    public void before(){
        folderRepository.deleteAll();
        relationRepository.deleteAll();
        userRepository.deleteAll();

        Folder folder1 = new Folder("");
        User user1 = new User("user1", "user1Email@naver.com", folder1.get_id(), "user1Picture", "1234");
        folderRepository.save(folder1);
        userRepository.save(user1);
        Folder data2 = new Folder("");
        User user2 = new User("user2", "user2Email@naver.com", data2.get_id(), "user2Picture", "5678");
        folderRepository.save(data2);
        userRepository.save(user2);
        Folder data3 = new Folder("");
        User user3 = new User("user3", "user3Email@naver.com", data3.get_id(), "user3Picture", "1234");
        folderRepository.save(data3);
        userRepository.save(user3);
        Folder data4 = new Folder("");
        User user4 = new User("user4", "user4Email@naver.com", data4.get_id(), "user4Picture", "12345");
        folderRepository.save(data4);
        userRepository.save(user4);

        User temp1 = userRepository.findByNickname("user1").get();
        User temp2 = userRepository.findByNickname("user2").get();

        Relation d=new Relation(temp1.getId(),temp2.getId());
        User temp4 = userRepository.findByNickname("user4").get();
        Relation d1=new Relation(temp1.getId(),temp4.getId());
        d1.setRelationType(RelationType.BOTH);
        relationRepository.save(d);
        relationRepository.save(d1);
        user1.getRelations().add(d.getId());
        user1.getRelations().add(d1.getId());
        user2.getRelations().add(d.getId());
        user4.getRelations().add(d1.getId());
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user4);
    }

    @Test
    @DisplayName("테스트 01 : 친구목록, 신청목록 가져오기")
    public void 친구목록조회() throws URISyntaxException, JsonProcessingException {
        //given
        final String baseUrl = "http://localhost:" + port + "/friend?email=user1Email@naver.com";
        URI uri = new URI(baseUrl);
        //when
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode root = mapper.readTree(result.getBody());
        assertThat(root.path("numberOfRequest").asInt()).isEqualTo(0);
        assertThat(root.path("numberOfFriend").asInt()).isEqualTo(1);
        assertThat(root.path("friends").get(0).path("email").asText()).isEqualTo("user4Email@naver.com");
    }

    @Test
    @DisplayName("테스트 01-1 : 친구목록, 신청목록 가져오기, 데이터가 없음")
    public void 친구목록조회_친구가없음() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/friend?email=user3Email@naver.com";
        URI uri = new URI(baseUrl);
        //when
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 02 : 친구목록, 신청목록 가져오기, 없는 이메일")
    public void 친구목록조회_없는이메일() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/friend?email=user333Email@naver.com";
        URI uri = new URI(baseUrl);
        //when
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 03 : 친구 신청")
    public void 친구신청() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/friend";
        URI uri = new URI(baseUrl);
        CreateRelationRequestDto createRelationRequestDto = new CreateRelationRequestDto();
        createRelationRequestDto.setEmail("user1Email@naver.com");
        createRelationRequestDto.setFriendNickname("user3");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        User reciever = userRepository.findByEmail("user3Email@naver.com").get();
        User sender = userRepository.findByEmail("user1Email@naver.com").get();
        Relation relation = relationRepository.findById(reciever.getRelations().get(0)).get();
        assertThat(relation.getRelationType()).isEqualTo(RelationType.REQUEST);
        assertThat(relation.getReceiverId()).isEqualTo(reciever.getId());
        assertThat(relation.getSenderId()).isEqualTo(sender.getId());
        assertThat(reciever.getRelations().get(0)).isEqualTo(relation.getId());
        assertThat(sender.getRelations().get(2)).isEqualTo(relation.getId());
    }

    @Test
    @DisplayName("테스트 04 : 친구 신청, 이미 있는 신청")
    public void 친구신청_이미신청() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/friend";
        URI uri = new URI(baseUrl);
        CreateRelationRequestDto createRelationRequestDto = new CreateRelationRequestDto();
        createRelationRequestDto.setEmail("user1Email@naver.com");
        createRelationRequestDto.setFriendNickname("user2");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("테스트 05 : 친구 수락")
    public void 친구수락() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        acceptRelationRequestDto.setEmail("user2Email@naver.com");
        acceptRelationRequestDto.setFriendNickname("user1");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        User user2 = userRepository.findByNickname("user2").get();
        assertThat(relationRepository.findById(user2.getRelations().get(0)).get().getRelationType()).isEqualTo(RelationType.BOTH);
    }

    @Test
    @DisplayName("테스트 06 : 친구 수락, 내가 요청한 친구")
    public void 친구수락_내가요청_실패() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        acceptRelationRequestDto.setEmail("user1Email@naver.com");
        acceptRelationRequestDto.setFriendNickname("user2");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 07 : 친구 수락, 이미 수락한 친구")
    public void 친구수락_이미친구() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        acceptRelationRequestDto.setEmail("user4Email@naver.com");
        acceptRelationRequestDto.setFriendNickname("user1");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 08 : 친구 수락, 없는 사용자")
    public void 친구수락_없는사람() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        acceptRelationRequestDto.setEmail("user10Email@naver.com");
        acceptRelationRequestDto.setFriendNickname("user4");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 09 : 친구 수락, 없는 요청")
    public void 친구수락_없는요청() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        acceptRelationRequestDto.setEmail("user1");
        acceptRelationRequestDto.setFriendNickname("user3Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 10 : 친구 거절")
    public void 친구거절() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        RejectRelationRequestDto rejectRelationRequestDto = new RejectRelationRequestDto();
        rejectRelationRequestDto.setEmail("user2Email@naver.com");
        rejectRelationRequestDto.setFriendNickname("user1");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<RejectRelationRequestDto> request = new HttpEntity<>(rejectRelationRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        User user2 = userRepository.findByNickname("user2").get();
        assertThat(relationRepository.findById(user2.getRelations().get(0)).isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테스트 11 : 친구 거절, 내가 신청한 요청")
    public void 친구거절_내가요청() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        RejectRelationRequestDto rejectRelationRequestDto = new RejectRelationRequestDto();
        rejectRelationRequestDto.setEmail("user1Email@naver.com");
        rejectRelationRequestDto.setFriendNickname("user2");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<RejectRelationRequestDto> request = new HttpEntity<>(rejectRelationRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 12 : 친구 거절, 없는 사용자")
    public void 친구거절_없는사람() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend/request";
        URI uri = new URI(baseUrl);
        RejectRelationRequestDto rejectRelationRequestDto = new RejectRelationRequestDto();
        rejectRelationRequestDto.setEmail("user5Email@naver.com");
        rejectRelationRequestDto.setFriendNickname("user2");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<RejectRelationRequestDto> request = new HttpEntity<>(rejectRelationRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("테스트 13 : 친구 삭제")
    public void 친구삭제() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend";
        URI uri = new URI(baseUrl);
        DeleteFriendRequestDto deleteFriendRequestDto = new DeleteFriendRequestDto();
        deleteFriendRequestDto.setEmail("user1Email@naver.com");
        deleteFriendRequestDto.setFriendNickname("user4");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<DeleteFriendRequestDto> request = new HttpEntity<>(deleteFriendRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        User user4 = userRepository.findByNickname("user4").get();
        assertThat(user4.getRelations().isEmpty()).isEqualTo(true);
    }
    @Test
    @DisplayName("테스트 14 : 친구 삭제, 없는 요청")
    public void 친구삭제_없는요청() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend";
        URI uri = new URI(baseUrl);
        DeleteFriendRequestDto deleteFriendRequestDto = new DeleteFriendRequestDto();
        deleteFriendRequestDto.setEmail("user1Email@naver.com");
        deleteFriendRequestDto.setFriendNickname("user3");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<DeleteFriendRequestDto> request = new HttpEntity<>(deleteFriendRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("테스트 15 : 친구 삭제, 이미 삭제된 요청")
    public void 친구삭제_이미삭제() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/friend";
        URI uri = new URI(baseUrl);
        DeleteFriendRequestDto deleteFriendRequestDto = new DeleteFriendRequestDto();
        deleteFriendRequestDto.setEmail("user1Email@naver.com");
        deleteFriendRequestDto.setFriendNickname("user4");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        HttpEntity<DeleteFriendRequestDto> request = new HttpEntity<>(deleteFriendRequestDto,headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        User user4 = userRepository.findByNickname("user4").get();
        assertThat(user4.getRelations().isEmpty()).isEqualTo(true);

        ResponseEntity<String> result1 = this.restTemplate.exchange(uri, HttpMethod.DELETE,request,String.class);
        assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
