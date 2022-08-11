package PoolC.Comect.data;

import PoolC.Comect.domain.data.Folder;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DataService {
    private final DataRepository dataRepository;


    @Transactional
    public void createFolder( ObjectId id, String path, String name, String picture){
        Folder folder = new Folder(name, picture);
        dataRepository.createFolder(id, path, folder);
    }

    @Transactional
    public void deleteFolder(ObjectId id, String path){
        dataRepository.deleteFolder(id, path);
    }

    @Transactional
    public void updateFolderName(ObjectId id,String path, String newName){
        dataRepository.updateFolder(id,path,"name",newName);
    }

    @Transactional
    public void updateFolderPicture(ObjectId id,String path, String newPicture){
        dataRepository.updateFolder(id,path,"picture",newPicture);
    }

    @Transactional
    public void createLink( ObjectId id, String path, String name, String picture, String link){
        Folder folder = new Folder(name, picture);
        dataRepository.createLink(id, path, folder);
    }

    @Transactional
    public void deleteLink(ObjectId id, String path){
        dataRepository.deleteLink(id, path);
    }

    @Transactional
    public void updateLinkName(ObjectId id,String path, String newName){
        dataRepository.updateLink(id,path,"name",newName);
    }

    @Transactional
    public void updateLinkPicture(ObjectId id,String path, String newPicture){
        dataRepository.updateLink(id,path,"picture",newPicture);
    }
    @Transactional
    public void updateLinkLink(ObjectId id,String path, String newLink){
        dataRepository.updateLink(id,path,"link",newLink);
    }
}
