package PoolC.Comect.elasticFolder;

import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class elasticFolderTest {

    @Autowired
    ElasticFolderRepository elasticFolderRepository;

    @Before
    public void cleaning(){
        elasticFolderRepository.deleteAll();
    }


    @Test
    @DisplayName("elasticsearch test, 잘 들어가는지 확인")
    public void 일레스틱서치에넣기(){

        ElasticFolder elasticFolder1=new ElasticFolder(new ObjectId().toString(),"nickname","path","아카라카");
        ElasticFolder elasticFolder2=new ElasticFolder(new ObjectId().toString(),"nickname","path","풀씨 임원진");
        ElasticFolder elasticFolder3=new ElasticFolder(new ObjectId().toString(),"nickname","path","로아 직업별 티어표");
        ElasticFolder elasticFolder4=new ElasticFolder(new ObjectId().toString(),"nickname","path","풀씨 세미나");
        ElasticFolder elasticFolder5=new ElasticFolder(new ObjectId().toString(),"nickname","path","동아리 임원진");
        ElasticFolder elasticFolder6=new ElasticFolder(new ObjectId().toString(),"nickname","path","임승연");
        ElasticFolder elasticFolder7=new ElasticFolder(new ObjectId().toString(),"nickname","path","김성하");
        ElasticFolder elasticFolder8=new ElasticFolder(new ObjectId().toString(),"nickname","path","진영민");
        ElasticFolder elasticFolder9=new ElasticFolder(new ObjectId().toString(),"nickname","path","링키 프로젝트");
        ElasticFolder elasticFolder10=new ElasticFolder(new ObjectId().toString(),"nickname","path","컴포즈 커피 세일기간");
        ElasticFolder elasticFolder11=new ElasticFolder(new ObjectId().toString(),"nickname","path","메가커피 가격");
        ElasticFolder elasticFolder12=new ElasticFolder(new ObjectId().toString(),"nickname","path","핸드폰별 가격");
        ElasticFolder elasticFolder13=new ElasticFolder(new ObjectId().toString(),"nickname","path","나무 빨대는 왜 맛이 없을까");
        ElasticFolder elasticFolder14=new ElasticFolder(new ObjectId().toString(),"nickname","path","게임프로젝트");
        ElasticFolder elasticFolder15=new ElasticFolder(new ObjectId().toString(),"nickname","path","영민이의 우산은 어디로 갔을까");

        elasticFolderRepository.save(elasticFolder1);
        elasticFolderRepository.save(elasticFolder2);
        elasticFolderRepository.save(elasticFolder3);
        elasticFolderRepository.save(elasticFolder4);
        elasticFolderRepository.save(elasticFolder5);
        elasticFolderRepository.save(elasticFolder6);
        elasticFolderRepository.save(elasticFolder7);
        elasticFolderRepository.save(elasticFolder8);
        elasticFolderRepository.save(elasticFolder9);
        elasticFolderRepository.save(elasticFolder10);
        elasticFolderRepository.save(elasticFolder11);
        elasticFolderRepository.save(elasticFolder12);
        elasticFolderRepository.save(elasticFolder13);
        elasticFolderRepository.save(elasticFolder14);
        elasticFolderRepository.save(elasticFolder15);

    }
}
