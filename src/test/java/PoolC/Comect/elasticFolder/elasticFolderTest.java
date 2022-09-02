//package PoolC.Comect.elasticFolder;
//
//import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
//import PoolC.Comect.elasticFolder.service.ElasticService;
//import PoolC.Comect.folder.repository.FolderRepository;
//import PoolC.Comect.folder.service.FolderService;
//import PoolC.Comect.user.repository.UserRepository;
//import PoolC.Comect.user.service.UserService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.DisplayName.class)
//public class elasticFolderTest {
//
//    @Autowired
//    ElasticService elasticService;
//    @Autowired
//    FolderService folderService;
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ElasticFolderRepository elasticFolderRepository;
//    @Autowired
//    FolderRepository folderRepository;
//    @Autowired
//    UserRepository userRepository;
//
//    @Before
//    public void cleaning(){
//        elasticFolderRepository.deleteAll();
//    }
//
//    @Test
//    public void emptyTest(){
//
//    }
//
//
//    @Test
//    @DisplayName("elasticsearch test, 잘 들어가는지 확인")
//    public void 일레스틱서치에넣기(){
//
//        userRepository.deleteAll();
//        folderRepository.deleteAll();
//        elasticFolderRepository.deleteAll();
//
//        userService.join("sungha.kim123@gmail.com","1234","sungha","sunghaPicture");
//        userService.join("young.min123@gmail.com","1234","youngmin","youngminPicture");
//
//
//
//        folderService.folderCreate("sungha.kim123@gmail.com","","로아");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/","직업");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","블래");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/블래/","블래스터 카던 스킬트리");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","소서");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/소서/","소서리스 카오스던전 스킬트리");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/","디트");
//        folderService.folderCreate("sungha.kim123@gmail.com","로아/직업/디트/","디트 레이드 스킬트리");
//
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크","링크","그림",null,"true");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크1","링크","그림",null,"true");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크2","링크","그림",null,"true");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크3","링크","그림",null,"true");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크4","링크","그림",null,"true");
//        folderService.linkCreate("sungha.kim123@gmail.com","로아/직업/블래","블래스터링크5","링크","그림",null,"true");
////        folderService.folderCreate("young.min123@gmail.com","","로아");
////        folderService.folderCreate("young.min123@gmail.com","로아/","직업");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/","블래");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/블래/","블래스터 카던 스킬트리");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/","소서");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/소서/","소서리스 카오스던전 스킬트리");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/","디트");
////        folderService.folderCreate("young.min123@gmail.com","로아/직업/디트/","디트 레이드 스킬트리");
//
//
//
//
//
////        List<String> stringList=new ArrayList<>();
////        stringList.add("로아/");
//        //folderService.folderDelete("sungha.kim123@gmail.com",stringList);
//        //folderService.folderUpdate("sungha.kim123@gmail.com","로아/직업/","로ㄴㅇㄹㄴㅇㅁ");
// //       List<String> t=new ArrayList<>();
//  //      t.add("로아/직업/블래/");
////
//
//    }
//}
