package PoolC.Comect.folder.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
//import PoolC.Comect.elasticFolder.domain.ElasticFolder;
//import PoolC.Comect.elasticFolder.domain.ElasticLink;
//import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
//import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
//    private final ElasticFolderRepository elasticFolderRepository;
//    private final ElasticLinkRepository elasticLinkRepository;

    public void folderCreate(String userEmail, String path, String folderName){
        User user = getUserByEmail(userEmail);
        Folder folder = new Folder(folderName);
        folderRepository.folderCreate(user.getRootFolderId(), path, folder);
//        ElasticFolder elasticFolder=new ElasticFolder(user.getId().toString(),path+folderName+"/",folderName);
//        elasticFolderRepository.save(elasticFolder);
    }

    public Folder folderRead(String userEmail, String path){
        User user = getUserByEmail(userEmail);
        Folder folder=folderRepository.folderRead(user.getRootFolderId(),path);
        return folder;
    }

    public void folderUpdate(String userEmail, String path, String folderName){
        User user = getUserByEmail(userEmail);
        folderRepository.folderUpdate(user.getRootFolderId(),path,folderName);
        //elasticFolderRepository.update(user.getId().toString(),path,folderName);
    }

    @Transactional
    public void folderDelete(String userEmail, List<String> paths){
        User user = getUserByEmail(userEmail);
        for(String path:paths) {
            folderRepository.folderDelete(user.getRootFolderId(), path);
            //elasticFolderRepository.delete(user.getId().toString(),path);
        }
    }

    @Transactional
    public void folderMove(String userEmail, List<String> originalPaths, String modifiedPath){
        User user = getUserByEmail(userEmail);
        for(String originalPath:originalPaths){
            Folder folder=folderRepository.folderRead(user.getRootFolderId(),originalPath);
            if(folderRepository.checkPathFolder(user.getRootFolderId(),modifiedPath+"/"+folder.getName())) throw new CustomException(ErrorCode.FILE_CONFLICT);
            folderRepository.folderDelete(user.getRootFolderId(),originalPath);
            folderRepository.folderCreate(user.getRootFolderId(),modifiedPath,folder);
            //elasticFolderRepository.move(user.getId().toString(),originalPath,modifiedPath);
        }
    }

    public boolean folderCheckPath(String userEmail, String path){
        User user = getUserByEmail(userEmail);
        return folderRepository.checkPathFolder(user.getRootFolderId(), path);
    }

    public void linkCreate(String userEmail, String path, String name, String url, String imageUrl, List<String> keywords, String isPublic){
        User user = getUserByEmail(userEmail);
        Link link=new Link(name,imageUrl,url,keywords,isPublic);
        folderRepository.linkCreate(user.getRootFolderId(), path, link);
//        ElasticLink elasticLink= new ElasticLink(user.getId().toString(),path,link.get_id().toString(),isPublic,name);
//        elasticLinkRepository.save(elasticLink);
    }

    public Link linkRead(String userEmail,String path,String id){
        User user=getUserByEmail(userEmail);
        System.out.println(id);
        return folderRepository.linkRead(user.getRootFolderId(),path,new ObjectId(id));
    }

    public void linkUpdate(String id,String email,String path,String name,String url,String imageUrl,List<String> keywords,String isPublic){
        User user = getUserByEmail(email);
        Link link=new Link(name,imageUrl,url,keywords,isPublic);
        folderRepository.linkUpdate(user.getRootFolderId(), path,new ObjectId(id), link);
    }

    @Transactional
    public void linkDelete(String email,String path,List<String> ids){
        User user = getUserByEmail(email);
        for(int i=0;i<ids.size();++i){
            folderRepository.linkDelete(user.getRootFolderId(), path,new ObjectId(ids.get(i)));
        }
    }

    @Transactional
    public void linkMove(String userEmail, String originalPath, List<String> originalIds,String modifiedPath){
        User user = getUserByEmail(userEmail);
        for(int i=0;i<originalIds.size();++i){
            String originalId=originalIds.get(i);
            Link link=folderRepository.linkRead(user.getRootFolderId(),originalPath,new ObjectId(originalId));
            folderRepository.linkDelete(user.getRootFolderId(),originalPath,new ObjectId(originalId));
            folderRepository.linkCreate(user.getRootFolderId(),modifiedPath,link);
        }
    }

    private User getUserByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
