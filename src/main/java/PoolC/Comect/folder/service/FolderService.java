package PoolC.Comect.folder.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
//import PoolC.Comect.elasticFolder.domain.ElasticFolder;
//import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
//import PoolC.Comect.elasticFolder.service.ElasticFolderService;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.ImageUploadData;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    //private final ElasticFolderRepository elasticFolderRepository;

    public void folderCreate(String userEmail, String path, String folderName){
        User user = getUserByEmail(userEmail);
        Folder folder = new Folder(folderName);
        folderRepository.folderCreate(user.getRootFolderId(), path, folder);
        //ElasticFolder elasticFolder=new ElasticFolder(user.getId().toString(),path+folderName+"/",folderName);
        //elasticFolderRepository.save(elasticFolder);
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

    public boolean linkCreate(String userEmail, String path, String name, String url, MultipartFile multipartFile, List<String> keywords, String isPublic,Boolean imageChange){
        User user = getUserByEmail(userEmail);

        Image image = imageService.upLoad(multipartFile, userEmail);
        boolean changeSuccess=false;
        if(imageChange){
            ImageUploadData imageUploadData = imageService.imageToUrl(multipartFile, userEmail);
            user.setImageId(imageUploadData.getImageId());
            changeSuccess = imageUploadData.isSuccess();
        }

        Link link=new Link(name,image.getId(),url,keywords,isPublic);
        folderRepository.linkCreate(user.getRootFolderId(), path, link);
        return changeSuccess;
    }

    public Link linkRead(String userEmail,String path,String id){
        User user=getUserByEmail(userEmail);
        System.out.println(id);
        return folderRepository.linkRead(user.getRootFolderId(),path,new ObjectId(id));
    }

    public boolean linkUpdate(String id,String email,String path,String name,String url,MultipartFile multipartFile,List<String> keywords,String isPublic,Boolean imageChange){
        User user = getUserByEmail(email);

        Image image = imageService.upLoad(multipartFile, email);
        boolean changeSuccess=false;
        if(imageChange){
            ImageUploadData imageUploadData = imageService.imageToUrl(multipartFile, email);
            user.setImageId(imageUploadData.getImageId());
            changeSuccess = imageUploadData.isSuccess();
        }

        Link link=new Link(name,image.getId(),url,keywords,isPublic);
        folderRepository.linkUpdate(user.getRootFolderId(), path,new ObjectId(id), link);
        return changeSuccess;
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
