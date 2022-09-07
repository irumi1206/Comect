package PoolC.Comect.elasticFolder;

import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.elasticFolder.service.ElasticService;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.folder.service.FolderService;
import PoolC.Comect.image.repository.ImageRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class elasticFolderTest {

    @Autowired
    ElasticService elasticService;
    @Autowired
    FolderService folderService;
    @Autowired
    UserService userService;

    @Autowired
    ElasticFolderRepository elasticFolderRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ElasticLinkRepository elasticLinkRepository;
    @Autowired
    ImageRepository imageRepository;

    @Before
    public void cleaning(){
        elasticFolderRepository.deleteAll();
        elasticLinkRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        imageRepository.deleteAll();

    }

    @Test
    public void emptyTest(){

    }


    @Test
    @DisplayName("elasticsearch test, 잘 들어가는지 확인")
    public void 일레스틱서치에넣기(){

        userRepository.deleteAll();
        folderRepository.deleteAll();
        elasticFolderRepository.deleteAll();

        userService.join("sungha.kim123@gmail.com","1234","sungha",null);
        userService.join("young.min123@gmail.com","1234","youngmin",null);



        folderService.folderCreate("sungha.kim123@gmail.com","","유투브 스포츠");
        folderService.folderCreate("sungha.kim123@gmail.com","","로아");
        folderService.folderCreate("sungha.kim123@gmail.com","","메이플");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/","직업");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","블래");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터 카던 스킬트리");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","소서");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/소서/","소서리스 카오스던전 스킬트리");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","디트");
        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/디트/","디트 레이드 스킬트리");

//        List<String> d=new ArrayList<>();
//        d.add("나는");
//        d.add("비행기");
//        d.add("개인링");

//        folderService.folderUpdate("sungha.kim123@gmail.com","로아/직업/소서/","소서리스");
//        folderService.folderUpdate("sungha.kim123@gmail.com","유투브 스포츠/","나는 돌고래");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크","링크",null,d,"true",null);
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크1","링크",null,null,"true",null);
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크2","링크",null,null,"true",null);
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크3","링크",null,null,"true",null);
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크4","링크",null,null,"true",null);
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터링크5","링크",null,null,"true",null);

//        Folder folder=folderService.folderRead("sungha.kim123@gmail.com","로아/직업/블래/");
//        String id=folder.getLinks().get(0).get_id().toString();
////
//        List<String> stringList=new ArrayList<>();
//        stringList.add("유투브 스포츠/");
//        //folderService.linkMove("sungha.kim123@gmail.com","로아/직업/블래/",stringList,"메이플/");
//        folderService.folderDelete("sungha.kim123@gmail.com",stringList);


        //        folderService.folderCreate("young.min123@gmail.com","","로아");
//        folderService.folderCreate("young.min123@gmail.com","","로아");
//        folderService.folderCreate("young.min123@gmail.com","","메이플");
//        folderService.folderCreate("young.min123@gmail.com","로아/","직업");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/","블래");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/블래/","블래스터 카던 스킬트리");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/","소서");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/소서/","소서리스 카오스던전 스킬트리");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/","디트");
//        folderService.folderCreate("young.min123@gmail.com","로아/직업/디트/","디트 레이드 스킬트리");
//
//        folderService.linkCreate("young.min123@gmail.com","로아/직업/블래/","블래스터링크","링크",null,d,"true",null);
//        folderService.linkCreate("young.min123@gmail.com","로아/직업/블래/","블래스터링크1","링크",null,null,"true",null);
//        folderService.linkCreate("young.min123@gmail.com","로아/직업/블래/","블래스터링크2","링크",null,null,"true",null);
//        folderService.linkCreate("young.min123@gmail.com","로아/직업/블래/","블래스터링크3","링크",null,null,"true",null);
//        folderService.linkCreate("young.min123@gmail.com","로아/직업/블래/","블래스터링크4","링크",null,null,"true",null);
//        List<String> stringList=new ArrayList<>();
//        stringList.add("유투브 스포츠/");
//        folderService.folderMove("sungha.kim123@gmail.com",stringList,"메이플/");
        folderService.folderUpdate("sungha.kim123@gmail.com","유투브 스포츠/","유튜브");
 //       List<String> t=new ArrayList<>();
  //      t.add("로아/직업/블래/");
//

    }
}
