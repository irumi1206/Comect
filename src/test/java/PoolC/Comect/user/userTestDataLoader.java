package PoolC.Comect.user;

import PoolC.Comect.data.domain.Data;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.relation.domain.Relation;
import PoolC.Comect.relation.repository.RelationRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class userTestDataLoader implements CommandLineRunner {


    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.remove(new Query(),"data");
        mongoTemplate.remove(new Query(),"relation");
        mongoTemplate.remove(new Query(),"user");

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

        List<User> temp1=userRepository.findByUserNickname("user1");
        List<User> temp2=userRepository.findByUserNickname("user2");
        Relation d=new Relation(temp1.get(0).getId(),temp2.get(0).getId());
        relationRepository.save(d);
    }
}
