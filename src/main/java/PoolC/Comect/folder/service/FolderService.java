package PoolC.Comect.folder.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;
import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import PoolC.Comect.folder.domain.Link;
import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.repository.FolderRepository;
import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.service.ImageService;
import PoolC.Comect.user.domain.ImageUploadData;
import PoolC.Comect.user.domain.User;
import PoolC.Comect.user.repository.UserRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
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
    private final ElasticFolderRepository elasticFolderRepository;
    private final ElasticLinkRepository elasticLinkRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final UserService userService;

    public void folderCreate(String userEmail, String path, String folderName){
        User user = getUserByEmail(userEmail);
        Folder folder = new Folder(folderName);
        folderRepository.folderCreate(user.getRootFolderId(), path, folder);
        ElasticFolder elasticFolder=new ElasticFolder(user.getId().toString(),path+folderName+"/",folderName);
        System.out.println(elasticFolder);
        elasticFolderRepository.save(elasticFolder);
    }

    public Folder folderRead(String userEmail, String path){
        User user = getUserByEmail(userEmail);
        Folder folder=folderRepository.folderRead(user.getRootFolderId(),path);
        return folder;
    }

    public void folderUpdate(String userEmail, String path, String folderName){
        User user = getUserByEmail(userEmail);
        folderRepository.folderUpdate(user.getRootFolderId(),path,folderName);
        elasticFolderRepository.update(user.getId().toString(),path,folderName);
        elasticLinkRepository.updateFolder(user.getId().toString(),path,folderName);
    }

    @Transactional
    public void folderDelete(String userEmail, List<String> paths){
        User user = getUserByEmail(userEmail);
        for(String path:paths) {
            elasticFolderRepository.delete(user.getId().toString(),path);
            elasticLinkRepository.deleteFolder(user.getId().toString(),path);

            Criteria criteria =Criteria.where("ownerId").matches(user.getId().toString());
            Query query=new CriteriaQuery(criteria);
            SearchHits<ElasticLink> elasticLinkSearchHits = elasticsearchOperations.search(query, ElasticLink.class);

            for(SearchHit<ElasticLink> elasticLinkSearchHit : elasticLinkSearchHits){
                ElasticLink elasticLink=elasticLinkSearchHit.getContent();
                if(!elasticLink.getPath().startsWith(path)) continue;
                String linkEmail=userService.findOneId(new ObjectId(user.getId().toString())).getEmail();
                Link link=linkRead(linkEmail,elasticLink.getPath(),elasticLink.getLinkId());
                imageService.deleteImage(link.getImageId(),user.getId().toString());
            }

            folderRepository.folderDelete(user.getRootFolderId(), path);
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
            elasticFolderRepository.move(user.getId().toString(),originalPath,modifiedPath);
            elasticLinkRepository.moveFolder(user.getId().toString(),originalPath,modifiedPath);
        }
    }

    public boolean folderCheckPath(String userEmail, String path){
        User user = getUserByEmail(userEmail);
        return folderRepository.checkPathFolder(user.getRootFolderId(), path);
    }

    public boolean linkCreate(String userEmail, String path, String name, String url, MultipartFile multipartFile, List<String> keywords, String isPublic, String imageUrl){
        User user = getUserByEmail(userEmail);

        boolean changeSuccess;
        Link link;
        if(multipartFile!=null && !multipartFile.isEmpty()){
            ImageUploadData imageUploadData = imageService.createImage(multipartFile, userEmail, user.getId().toString());
            changeSuccess = imageUploadData.isSuccess();
            link=new Link(name,imageUploadData.getImageUrl(),url,keywords,isPublic);
        }else{
            link=new Link(name,imageUrl,url,keywords,isPublic);
            changeSuccess=true;
        }

        String keywordString="";
        if(keywords!=null) for(String keyword : keywords) keywordString+=keyword+" ";

        folderRepository.linkCreate(user.getRootFolderId(), path, link);
        ElasticLink elasticLink= new ElasticLink(user.getId().toString(),path,isPublic,link.get_id().toString(),name,keywordString);
        elasticLinkRepository.save(elasticLink);
        return changeSuccess;
    }

    public Link linkRead(String userEmail,String path,String id){
        User user=getUserByEmail(userEmail);
        System.out.println(user);
        System.out.println(userEmail);
        return folderRepository.linkRead(user.getRootFolderId(),path,new ObjectId(id));
    }

    public boolean linkUpdate(String id,String email,String path,String name,String url,MultipartFile multipartFile,List<String> keywords,String isPublic,String imageChange,String imageUrl){
        User user = getUserByEmail(email);

        boolean changeSuccess=true;
        if(imageChange.equals("true")){
            Link link;
            if(multipartFile!=null && !multipartFile.isEmpty()) {
                ImageUploadData imageUploadData = imageService.createImage(multipartFile, email,user.getId().toString());
                changeSuccess = imageUploadData.isSuccess();
                link=new Link(name,imageUploadData.getImageUrl(),url,keywords,isPublic);
            }else{
                link=new Link(name,imageUrl,url,keywords,isPublic);
            }
            folderRepository.linkUpdate(user.getRootFolderId(), path,new ObjectId(id), link);
            String keywordString="";
            if(keywords!=null) for(String keyword : keywords) keywordString+=keyword+" ";
            elasticLinkRepository.update(user.getId().toString(),path,id,link.get_id().toString(),name,keywordString,isPublic);
        }else{
            String originalImageUrl=linkRead(email,path,id).getImageUrl();
            Link link=new Link(name,originalImageUrl,url,keywords,isPublic);
            folderRepository.linkUpdate(user.getRootFolderId(), path,new ObjectId(id), link);
            String keywordString="";
            if(keywords!=null) for(String keyword : keywords) keywordString+=keyword+" ";
            elasticLinkRepository.update(user.getId().toString(),path,id,link.get_id().toString(),name,keywordString,isPublic);
        }
        return changeSuccess;
    }

    @Transactional
    public void linkDelete(String email,String path,List<String> ids){
        User user = getUserByEmail(email);
        for(int i=0;i<ids.size();++i){
            ObjectId imageId=linkRead(email,path,ids.get(i)).getImageId();
            if(imageId!=null){
                imageService.deleteImage(imageId,user.getId().toString());
            }
            folderRepository.linkDelete(user.getRootFolderId(), path,new ObjectId(ids.get(i)));
            elasticLinkRepository.delete(user.getId().toString(),path,ids.get(i));
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
            elasticLinkRepository.move(user.getId().toString(),originalPath,originalIds.get(i),modifiedPath);
        }
    }

    private User getUserByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
