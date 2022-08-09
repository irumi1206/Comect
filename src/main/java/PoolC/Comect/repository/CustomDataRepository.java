package PoolC.Comect.repository;

import PoolC.Comect.domain.data.Folder;
import PoolC.Comect.domain.data.Link;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDataRepository {
    public void createFolder(ObjectId rootId,String path, Folder folder);
    public List<Folder> readFolder(ObjectId rootId, String path);
    public void updateFolder(ObjectId rootId,String path,String key,String value);
    public void deleteFolder(ObjectId rootId,String path);
}
