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
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        createUser1();
        createUser2();
        createUser3();
        linkUser13();
        linkUser23();
        setData();

    }

    private void createUser1() {
        Data data1=new Data("root","user1Picture");
        dataRepository.save(data1);
        ObjectId tempId=data1.getId();
        User user1=new User("user1","user1Email",tempId,"userPicture1");
        userRepository.save(user1);
    }
    private void createUser2() {
        Data data2=new Data("root","user2Picture");
        dataRepository.save(data2);
        User user2=new User("user2","user2Email",data2.getId(),"userPicture2");
        userRepository.save(user2);
    }
    private void createUser3() {
        Data data3=new Data("root","user3Picture");
        dataRepository.save(data3);
        User user3=new User("user3","user3Email",data3.getId(),"userPicture3");
        userRepository.save(user3);
    }

    private void linkUser13(){
        User user1=userRepository.findByEmail("user1Email").get(0);
        ObjectId id1 = user1.getId();
        User user2=userRepository.findByEmail("user2Email").get(0);
        ObjectId id2 = user2.getId();
        Relation relation = new Relation(id1, id2);
        relationRepository.save(relation);
        user1.addRelation(relation);
        user2.addRelation(relation);
        userRepository.save(user1);
        userRepository.save(user2);
    }
    private void linkUser23(){
        User user2=userRepository.findByEmail("user2Email").get(0);
        ObjectId id2 = user2.getId();
        User user3=userRepository.findByEmail("user3Email").get(0);
        ObjectId id3 = user3.getId();
        Relation relation = new Relation(id2, id3);
        relationRepository.save(relation);
        user2.addRelation(relation);
        user3.addRelation(relation);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    private void setData(){
        User user=userRepository.findByEmail("user1Email").get(0);
        ObjectId rootFolderId = user.getRootFolderId();
        Optional<Data> root = dataRepository.findById(rootFolderId);
        System.out.println(root);
        Folder folder =new Folder("folder1","folder1Picture");
        dataRepository.insertToFolder(root.get().getId(),folder);
        System.out.println(folder);

//        Folder folder11=new Folder("folder11","fo1derP1cture11");
//        Folder folder12=new Folder("folder12","folderPicture12");
//        Folder folder13=new Folder("folder13","folderPicture13");
//        Link link11 = new Link("link11","linkPicture11","link.11");
//        Link link12 = new Link("link12","linkPicture12","link.12");
//        Link link13 = new Link("link13","linkPicture13","link.13");
//        Link link14 = new Link("link14","linkPicture14","link.14");
//
//        System.out.println(rootFolderId);
//        Optional<Data> root = dataRepository.findById(rootFolderId);
//        root.get().addFolder(folder11);
//        root.get().addFolder(folder12);
//        root.get().addLink(link11);
//
//        folder11.addLink(link12);
//        folder12.addFolder(folder13);
//        folder12.addLink(link14);
//        folder12.addLink(link13);
//        dataRepository.save(root.get());
//        folder11.setParentFolderId(rootFolderId);
//        folder12.setParentFolderId(rootFolderId);
//        link13.setParentFolderId(folder12.getId());
//        link11.setParentFolderId(rootFolderId);
//        link12.setParentFolderId(folder11.getId());
//        folder13.setParentFolderId(folder12.getId());
//        link14.setParentFolderId(folder12.getId());
//        dataRepository.save(root.get());
    }

}
