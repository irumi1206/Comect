package PoolC.Comect.folder.service;

import PoolC.Comect.common.CustomException;
import PoolC.Comect.common.ErrorCode;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.dto.FolderInfo;
import PoolC.Comect.folder.dto.LinkInfo;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Transactional
    public void folderCreate(String userEmail, String path, String folderName){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Folder folder = new Folder(folderName);
        folderRepository.folderCreate(user.getRootFolderId(), path, folder);
    }

    @Transactional
    public Folder folderRead(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Folder folder=folderRepository.folderRead(user.getRootFolderId(),path);
        return folder;
    }

    @Transactional
    public void folderUpdate(String userEmail, String path, String folderName){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        folderRepository.folderUpdate(user.getRootFolderId(),path,folderName);
    }

    @Transactional
    public void folderDelete(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        folderRepository.folderDelete(user.getRootFolderId(),path);
    }

    @Transactional
    public void folderMove(String userEmail, List<String> originalPaths, String modifiedPath){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        for(String originalPath:originalPaths){
            Folder folder=folderRepository.folderRead(user.getRootFolderId(),originalPath);
            folderRepository.folderDelete(user.getRootFolderId(),originalPath);
            folderRepository.folderCreate(user.getRootFolderId(),modifiedPath,folder);
        }
    }

    @Transactional
    public boolean folderCheckPath(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        return folderRepository.checkPathFolder(user.getRootFolderId(), path);
    }

    @Transactional
    public void linkCreate(String userEmail, String path, String name, String url, String imageUrl, List<String> keywords, String isPublic){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Link link=new Link(name,imageUrl,url,keywords,isPublic);
        folderRepository.linkCreate(user.getRootFolderId(), path, link);
    }

    @Transactional
    public void linkUpdate(String id,String email,String path,String name,String url,String imageUrl,List<String> keywords,String isPublic){
        validateEmail(email);
        User user = getUserByEmail(email);
        Link link=new Link(name,imageUrl,url,keywords,isPublic);
        folderRepository.linkUpdate(user.getRootFolderId(), path,new ObjectId(id), link);
    }

    @Transactional
    public void linkDelete(String email,String path,String id){
        validateEmail(email);
        User user = getUserByEmail(email);
        folderRepository.linkDelete(user.getRootFolderId(), path,new ObjectId(id));
    }

    private User getUserByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateEmail(String userEmail){
        String regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(userEmail);
        if(!matcher.matches()){
            throw new CustomException(ErrorCode.EMAIL_NOT_VALID);
        }
    }

}
