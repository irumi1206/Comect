package PoolC.Comect.image.service;

import PoolC.Comect.common.exception.CustomException;
import PoolC.Comect.common.exception.ErrorCode;
import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.domain.ReadImageDomain;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.user.domain.ImageUploadData;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.elasticsearch.cluster.ClusterState;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void deleteImage(ObjectId id,String userId){
        try{
            Image image = imageRepository.findById(id).get();
            if(image.getOwnerId().equals(userId)){
                Files.delete(Paths.get("imageStorage/"+id.toHexString()));
                imageRepository.deleteById(id);
            }
        } catch (Exception e) {
        }
    }

    public ImageUploadData createImage(MultipartFile multipartFile,String email,String ownerId){
        ImageUploadData imageUploadData = new ImageUploadData();
        if(multipartFile!=null && !multipartFile.isEmpty()){
            Image image = new Image(multipartFile.getContentType(),email,ownerId);
            String upLoadDir = "imageStorage";
            try{
                saveFile(upLoadDir,image.getId().toHexString(),multipartFile);
                imageRepository.save(image);
                imageUploadData.setImageUrl("http://119.71.1.161:8081/image?id="+image.getId().toHexString());
            }catch(Exception e){
                imageUploadData.setSuccess(false);
            }
        }
        return imageUploadData;
    }

    public Image findById(ObjectId id){
        return imageRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.IMAGE_NOT_FOUND));
    }

    public ReadImageDomain readImage(ObjectId id){
        Image image = imageRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
//        if(!image.getEmail().equals(email)){
//            throw new CustomException(ErrorCode.NOT_MY_IMAGE);
//        }
        Path path=Paths.get("imageStorage/"+image.getId().toHexString());
        String contentType=image.getExtender();
        Resource resource;
        try{
            resource = new InputStreamResource(Files.newInputStream(path));
        }catch(IOException ioe){
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
        return new ReadImageDomain(contentType,resource);
    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile){
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            try{
                Files.createDirectories(uploadPath);
            }catch(IOException ioe){
                throw new CustomException(ErrorCode.IMAGE_SAVE_CANCELED);
            }
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new CustomException(ErrorCode.IMAGE_SAVE_CANCELED);
        }
    }
    public static ObjectId urlToId(String url){
        String[] split = url.split("id=");
        ObjectId objectId;
        try{
            objectId = new ObjectId(split[split.length-1]);
        }catch(Exception e){
            objectId=new ObjectId();
        }
        return objectId;
    }
}
