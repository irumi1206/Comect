package PoolC.Comect.relation;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.relation.domain.Relation;
import PoolC.Comect.relation.domain.RelationType;
import PoolC.Comect.relation.dto.AcceptRelationRequestDto;
import PoolC.Comect.relation.dto.CreateRelationRequestDto;
import PoolC.Comect.relation.dto.ReadRelationRequestDto;
import PoolC.Comect.relation.repository.RelationRepository;
import PoolC.Comect.user.domain.User;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
    private UserService userService;
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private UserRepository userRepository;


    @Before
    public void before(){
        dataRepository.deleteAll();
        relationRepository.deleteAll();
        userRepository.deleteAll();

        Data data1 = new Data();
        User user1 = new User("user1", "user1Email@naver.com", data1.getId(), "user1Picture", "1234");
        dataRepository.save(data1);
        userRepository.save(user1);
        Data data2 = new Data();
        User user2 = new User("user2", "user2Email@naver.com", data2.getId(), "user2Picture", "5678");
        dataRepository.save(data2);
        userRepository.save(user2);
        Data data3 = new Data();
        User user3 = new User("user3", "user3Email@naver.com", data1.getId(), "user3Picture", "1234");
        dataRepository.save(data3);
        userRepository.save(user3);
        Data data4 = new Data();
        User user4 = new User("user4", "user4Email@naver.com", data1.getId(), "user4Picture", "12345");
        dataRepository.save(data4);
        userRepository.save(user4);

        List<User> temp1=userRepository.findByUserNickname("user1");
        List<User> temp2=userRepository.findByUserNickname("user2");
        Relation d=new Relation(temp1.get(0).getId(),temp2.get(0).getId());
        List<User> temp4=userRepository.findByUserNickname("user4");
        Relation d1=new Relation(temp1.get(0).getId(),temp4.get(0).getId());
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
    public void friendList1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/find";
        URI uri = new URI(baseUrl);
        ReadRelationRequestDto readRelationRequestDto = new ReadRelationRequestDto();
        readRelationRequestDto.setUserEmail("user1Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 01-1 : 친구목록, 신청목록 가져오기, 데이터가 없음")
    public void friendList2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/find";
        URI uri = new URI(baseUrl);
        ReadRelationRequestDto readRelationRequestDto = new ReadRelationRequestDto();
        readRelationRequestDto.setUserEmail("user3Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 02 : 친구목록, 신청목록 가져오기, 없는 이메일")
    public void friendList3() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/find";
        URI uri = new URI(baseUrl);
        ReadRelationRequestDto readRelationRequestDto = new ReadRelationRequestDto();
        readRelationRequestDto.setUserEmail("user1E111mail@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 03 : 친구 신청")
    public void friendRequest1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/add";
        URI uri = new URI(baseUrl);
        CreateRelationRequestDto createRelationRequestDto = new CreateRelationRequestDto();
        createRelationRequestDto.setUserEmail("user1Email@naver.com");
        createRelationRequestDto.setFriendEmail("user3Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 04 : 친구 신청, 이미 있는 신청")
    public void friendRequest2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/add";
        URI uri = new URI(baseUrl);
        CreateRelationRequestDto createRelationRequestDto = new CreateRelationRequestDto();
        createRelationRequestDto.setUserEmail("user1Email@naver.com");
        createRelationRequestDto.setFriendEmail("user2Email@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 05 : 친구 수락")
    public void friendAccept1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user2Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail);
        acceptRelationRequestDto.setRelationId(user.getRelations().get(0).toHexString());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("테스트 06 : 친구 수락, 내가 요청한 친구")
    public void friendAccept2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user1Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail);
        acceptRelationRequestDto.setRelationId(user.getRelations().get(0).toHexString());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 07 : 친구 수락, 이미 수락한 친구")
    public void friendAccept3() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user4Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail);
        acceptRelationRequestDto.setRelationId(user.getRelations().get(0).toHexString());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 08 : 친구 수락, 없는 유저")
    public void friendAccept4() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user2Email@naver.com";
        String userEmail1 = "user5Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail1);
        acceptRelationRequestDto.setRelationId(user.getRelations().get(0).toHexString());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 09 : 친구 수락, 없는 요청")
    public void friendAccept5() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user2Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail);
        acceptRelationRequestDto.setRelationId("62fb65146db1947f98356363");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("테스트 10 : 친구 거절")
    public void friendReject1() throws URISyntaxException{
        //given
        final String baseUrl = "http://localhost:" + port + "/member/accept";
        URI uri = new URI(baseUrl);
        AcceptRelationRequestDto acceptRelationRequestDto = new AcceptRelationRequestDto();
        String userEmail = "user2Email@naver.com";
        User user = userService.findOne(userEmail);
        acceptRelationRequestDto.setUserEmail(userEmail);
        acceptRelationRequestDto.setRelationId(user.getRelations().get(0).toHexString());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,acceptRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
