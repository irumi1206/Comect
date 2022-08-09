package PoolC.Comect;

import PoolC.Comect.domain.data.Data;
import PoolC.Comect.domain.data.Folder;
import PoolC.Comect.domain.data.Link;
import PoolC.Comect.domain.relation.Relation;
import PoolC.Comect.domain.user.User;
import PoolC.Comect.repository.DataRepository;
import PoolC.Comect.repository.RelationRepository;
import PoolC.Comect.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("InitialDataLoader")
public class InitialDataLoader implements CommandLineRunner {
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

        createUser();
        createRelation();
        createFolder();


        Folder folder4=new Folder("folder4","folder4Picture");
        List<User> user1 = userRepository.findByUserNickname("user1");
        Data data1 = dataRepository.findById(user1.get(0).getRootFolderId()).get();
        dataRepository.createFolder(data1.getId(),"folder1/folder3",folder4);
        dataRepository.updateFolder(data1.getId(),"folder1/folder3/folder4","name","myFolder");
        dataRepository.deleteFolder(data1.getId(),"folder1/folder3");
    }
    void createUser(){
        Data data1=new Data();
        User user1=new User("user1","user1Email",data1.getId(),"user1Picture");
        dataRepository.save(data1);
        userRepository.save(user1);
        Data data2=new Data();
        User user2=new User("user2","user2Email",data2.getId(),"user2Picture");
        dataRepository.save(data2);
        userRepository.save(user2);
    }
    void createRelation(){
        List<User> temp1=userRepository.findByUserNickname("user1");
        List<User> temp2=userRepository.findByUserNickname("user2");
        Relation d=new Relation(temp1.get(0).getId(),temp2.get(0).getId());
        relationRepository.save(d);
    }
    void createFolder(){
        List<User> user1 = userRepository.findByUserNickname("user1");
        Data data1 = dataRepository.findById(user1.get(0).getRootFolderId()).get();
        Folder folder1 = new Folder("folder1","folder1Picture");
        Folder folder2 = new Folder("folder2","folder2Picture");
        Folder folder3 = new Folder("folder3","folder3Picture");
        Link link1 = new Link("link1","link1Picture","link1");
        Link link2 = new Link("link2","link2Picture","link2");
        Link link3 = new Link("link3","link3Picture","link3");
        Link link4 = new Link("link4","link4P1cture","link4");

        folder1.getLinks().add(link1);
        folder1.getLinks().add(link2);
        folder3.getLinks().add(link3);
        data1.getLinks().add(link4);

        folder1.getFolders().add(folder3);
        data1.getFolders().add(folder1);
        data1.getFolders().add(folder2);

        dataRepository.save(data1);
    }
}