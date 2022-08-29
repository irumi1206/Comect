package PoolC.Comect.image.service;

import PoolC.Comect.exception.CustomException;
import PoolC.Comect.exception.ErrorCode;
import PoolC.Comect.image.domain.Image;
import PoolC.Comect.image.domain.ReadImageDomain;
import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;

    @Transactional
    public void deleteImage(ObjectId id){
        try{
            Files.delete(Paths.get("imageStorage/"+id.toHexString()));
        }catch(IOException ioe){
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
        Image image = imageRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
//        if(!image.getEmail().equals(email)){
//            throw new CustomException(ErrorCode.NOT_MY_IMAGE);
//        }
        imageRepository.delete(image);
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
        return new ReadImageDomain(contentType,resource,image.getImageName());
    }

    public ObjectId upLoad(String imageName, MultipartFile multipartFile, String email){
        //validate check
        userService.findOne(email);
        Image image = new Image(imageName,multipartFile.getContentType(),email);
        String upLoadDir = "imageStorage";
        imageRepository.save(image);
        try{
            saveFile(upLoadDir,image.getId().toHexString(),multipartFile);
        }catch(Exception e){
            throw new CustomException(ErrorCode.IMAGE_SAVE_CANCELED);
        }
        return image.getId();
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
}
