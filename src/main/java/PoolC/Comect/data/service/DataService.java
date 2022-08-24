package PoolC.Comect.data.service;

import PoolC.Comect.common.CustomException;
import PoolC.Comect.common.ErrorCode;
import PoolC.Comect.data.domain.Link;
import PoolC.Comect.data.dto.FolderInfo;
import PoolC.Comect.data.dto.LinkInfo;
import PoolC.Comect.data.repository.DataRepository;
import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;
    private final UserRepository userRepository;

    @Transactional
    public void folderCreate(String userEmail, String path, String folderName){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Folder folder = new Folder(folderName);
        dataRepository.folderCreate(user.getRootFolderId(), path, folder);
    }

    @Transactional
    public int folderCheckPath(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        return dataRepository.checkPath(user.getRootFolderId(), path);
    }

    @Transactional
    public void linkCreate(String userEmail, String path, String linkTitle, String linkImage, String linkUrl, List<String> keywords, String isPublic){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Link link=new Link(linkTitle,linkImage,linkUrl,keywords,isPublic);
        dataRepository.linkCreate(user.getRootFolderId(), path, link);
    }

    @Transactional
    public List<LinkInfo> folderReadLink(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        List<LinkInfo> linkInfos = LinkInfo.toLinkInfo(dataRepository.folderReadLink(user.getRootFolderId(),path));
        return linkInfos;
    }

    @Transactional
    public List<FolderInfo> folderReadFolder(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        List<FolderInfo> folderInfos=FolderInfo.toFolderInfo(dataRepository.folderReadFolderName(user.getRootFolderId(),path));
        return folderInfos;
    }

    @Transactional
    public void folderUpdate(String userEmail, String path, String folderName){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        dataRepository.folderUpdate(user.getRootFolderId(),path,folderName);
    }

    @Transactional
    public void folderDelete(String userEmail, String path){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        dataRepository.folderDelete(user.getRootFolderId(),path);
    }

    @Transactional
    public void folderMove(String userEmail, String originalPath, String modifiedPath){
        validateEmail(userEmail);
        User user = getUserByEmail(userEmail);
        Folder folder=dataRepository.folderRead(user.getRootFolderId(),originalPath);
        dataRepository.folderDelete(user.getRootFolderId(),originalPath);
        dataRepository.folderCreate(user.getRootFolderId(),modifiedPath,folder);
    }

    private User getUserByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.orElseThrow(()->new CustomException(ErrorCode.EMAIL_NOT_FOUND));
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
