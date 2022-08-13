package PoolC.Comect.relation;

import PoolC.Comect.relation.dto.CreateRelationRequestDto;
import PoolC.Comect.relation.dto.ReadRelationRequestDto;
import PoolC.Comect.relation.service.RelationService;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.dto.CreateUserRequestDto;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.userTestDataLoader;
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
public class relationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void before() throws Exception {
        mongoTemplate.remove(new Query(),"data");
        mongoTemplate.remove(new Query(),"relation");
        mongoTemplate.remove(new Query(),"user");
        userTestDataLoader userTestDataLoader = new userTestDataLoader();
        userTestDataLoader.run();
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
    @DisplayName("테스트 02 : 친구목록, 신청목록 가져오기, 없는 이메일")
    public void friendList2() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/find";
        URI uri = new URI(baseUrl);
        ReadRelationRequestDto readRelationRequestDto = new ReadRelationRequestDto();
        readRelationRequestDto.setUserEmail("user1E111mail@naver.com");
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,readRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("테스트 03 : 친구 신청")
    public void friendRequest1() throws URISyntaxException {
        //given
        final String baseUrl = "http://localhost:" + port + "/member/add";
        URI uri = new URI(baseUrl);
        CreateRelationRequestDto createRelationRequestDto = new CreateRelationRequestDto();
        createRelationRequestDto.setUserEmail("user1Email@naver.com");
        User user3 = userRepository.findByEmail("user3Email@naver.com").get();
        createRelationRequestDto.setFriendId(user3.getId());
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
        User user2 = userRepository.findByEmail("user2Email@naver.com").get();
        createRelationRequestDto.setFriendId(user2.getId());
        //when
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri,createRelationRequestDto,String.class);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
