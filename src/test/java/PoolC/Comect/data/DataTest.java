package PoolC.Comect.data;

import PoolC.Comect.data.dto.FolderCreateRequestDto;
import PoolC.Comect.data.repository.DataRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    DataRepository dataRepository;

    @Test
    @DisplayName("테스트 01 : 폴더 생성실패, 폴더이름 형식오류")
    public void 폴더생성실패1() throws URISyntaxException {

        //given
        String baseUrl="http://localhost:" + port + "/folder/create";
        URI uri =new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("JSON","true");
        String userEmail="user1Email";
        String path="";
        String folderName="user1Folder";
        String folderPicture="user1FolderPicture";
        FolderCreateRequestDto createFolderRequest= FolderCreateRequestDto.builder()
                .userEmail((userEmail))
                .path(path)
                .folderName(folderName)
                .folderPicture(folderPicture)
                .build();

        //when
        HttpEntity<FolderCreateRequestDto> request = new HttpEntity<>(createFolderRequest,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,request,String.class);

        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    @DisplayName("테스트 02 : 폴더 생성실패, 경로형식 오류")
    public void 폴더생성실패2(){

    }

    @Test
    @DisplayName("테스트 03 : 폴더 생성실패, 존재하지 않는 경로")
    public void 폴더생성실패3(){

    }

    @Test
    @DisplayName("테스트 04 : 폴더 생성성공")
    public void 폴더생성성공(){

    }

    @Test
    @DisplayName("테스트 05 : 폴더 조회실패, 경로형식 오류")
    public void 폴더조회실패1(){

    }

    @Test
    @DisplayName("테스트 06 : 폴더 조회실패, 존재하지 않는 경로")
    public void 폴더조회실패2(){

    }

    @Test
    @DisplayName("테스트 07 : 폴더 조회성공")
    public void 폴더조회성공(){

    }

    @Test
    @DisplayName("테스트 08 : 폴더 수정실패, 폴더 경로형식 오류")
    public void 폴더수정실패1(){

    }

    @Test
    @DisplayName("테스트 09 : 폴더 수정실패, 존재하지 않는 폴더 경로 오류")
    public void 폴더수정실패2(){

    }

    @Test
    @DisplayName("테스트 10 : 폴더 수정실패, 폴더이름 형식 오류")
    public void 폴더수정실패3(){

    }

    @Test
    @DisplayName("테스트 11 : 폴더 수정성공")
    public void 폴더수정성공(){

    }

    @Test
    @DisplayName("테스트 10 : 폴더 삭제실패, 폴더경로 형식 오류")
    public void 폴더삭제실패1(){

    }

    @Test
    @DisplayName("테스트 11 : 폴더 삭제실패, 존재하지 않는 폴더 경로")
    public void 폴더삭제실패2(){

    }

    @Test
    @DisplayName("테스트 12 : 폴더 삭제성공")
    public void 폴더삭제성공(){

    }

    @Test
    @DisplayName("테스트 13 : 폴더 이동실패, original 폴더 경로 형식 오류")
    public void 폴더이동실패1(){

    }

    @Test
    @DisplayName("테스트 14 : 폴더 이동실패, 존재하지 않는 original 폴더 경로")
    public void 폴더이동실패2(){

    }

    @Test
    @DisplayName("테스트 15 : 폴더 이동실패, modified 폴더 경로 형식 오류")
    public void 폴더이동실패3(){

    }

    @Test
    @DisplayName("테스트 16 : 폴더 이동실패, 존재하지 않는 modified 폴더 경로")
    public void 폴더이동실패4(){

    }

    @Test
    @DisplayName("테스트 17 : 폴더 이동 성공")
    public void 폴더이동성공(){

    }

    @Test
    @DisplayName("테스트 18 : 링크 생성 실패, 경로 형식 오류")
    public void 링크생성실패1(){

    }

    @Test
    @DisplayName("테스트 19 : 링크 생성 실패, 존재하지 않는 경로")
    public void 링크생성실패2(){

    }

    @Test
    @DisplayName("테스트 20 : 링크 생성 실패, 링크 이름 형식 오류")
    public void 링크생성실패3(){

    }

    @Test
    @DisplayName("테스트 21 : 링크 생성성공")
    public void 링크생성성공(){

    }

    @Test
    @DisplayName("테스트 22 : 링크 조회 실패, 경로 형식 오류")
    public void 링크조회실패1(){

    }

    @Test
    @DisplayName("테스트 23 : 링크 조회 실패, 존재하지 않은 경로")
    public void 링크조회실패2(){

    }

    @Test
    @DisplayName("테스트 24 : 링크 조회성공")
    public void 링크조회성공(){

    }

    @Test
    @DisplayName("테스트 25 : 링크 수정 실패, 경로 형식 오류")
    public void 링크수정실패1(){

    }

    @Test
    @DisplayName("테스트 26 : 링크 수정 실패, 존재하지 않는 경로")
    public void 링크수정실패2(){

    }

    @Test
    @DisplayName("테스트 27 : 링크 수정 실패, 링크 이름 형식 오류")
    public void 링크수정실패3(){

    }

    @Test
    @DisplayName("테스트 28 : 링크 수정 성공")
    public void 링크수정성공(){

    }

    @Test
    @DisplayName("테스트 29 : 링크 삭제 실패, 경로 형식 오류")
    public void 링크삭제실패1(){

    }

    @Test
    @DisplayName("테스트 30 : 링크 삭제 실패, 존재하지 않는 경로")
    public void 링크삭제실패2(){

    }

    @Test
    @DisplayName("테스트 31 : 링크 삭제 성공")
    public void 링크삭제성공(){

    }

    @Test
    @DisplayName("테스트 32 : 링크 이동 실패, original 경로 형식 오류")
    public void 링크이동실패1(){

    }

    @Test
    @DisplayName("테스트 33 : 링크 이동 실패, 존재하지 않는 original 경로")
    public void 링크이동실패2(){

    }

    @Test
    @DisplayName("테스트 34 : 링크 이동 실패, modified 경로 형식 오류")
    public void 링크이동실패3(){

    }

    @Test
    @DisplayName("테스트 35 : 링크 이동 실패, 존재하지 않는 modified 경로")
    public void 링크이동실패4(){

    }

    @Test
    @DisplayName("테스트 32 : 링크 이동 성공")
    public void 링크이동성공(){

    }






}
