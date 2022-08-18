package PoolC.Comect.data.repository;

import PoolC.Comect.data.domain.Folder;
import PoolC.Comect.data.domain.Link;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDataRepository {

    public void folderCreate(ObjectId rootId,String path, Folder folder);
    public List<Link> folderReadLink(ObjectId rootId, String path);
    public List<String> folderReadFolder(ObjectId rootId, String path);
    public void folderUpdate(ObjectId rootId,String path,String folderName);
    public void folderDelete(ObjectId rootId,String path);

}
