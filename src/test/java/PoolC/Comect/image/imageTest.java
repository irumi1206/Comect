package PoolC.Comect.image;

import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class imageTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before(){
        userRepository.deleteAll();
        imageRepository.deleteAll();
        folderRepository.deleteAll();

        Folder folder1 = new Folder("");
        User user1 = new User("userOne", "user1Email@naver.com", folder1.get_id(), "user1Picture", "1234");
        folderRepository.save(folder1);
        userRepository.save(user1);
        Folder data2 = new Folder("");
        User user2 = new User("userTwo", "user2Email@naver.com", data2.get_id(), "user2Picture", "5678");
        folderRepository.save(data2);
        userRepository.save(user2);

    }
}