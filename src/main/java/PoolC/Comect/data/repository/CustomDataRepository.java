package PoolC.Comect.data.repository;

import PoolC.Comect.data.domain.Folder;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomDataRepository {
    public void createFolder(ObjectId rootId,String path, Folder folder);
    public List<Folder> readFolder(ObjectId rootId, String path);
    public void updateFolder(ObjectId rootId,String path,String key,String value);
    public void deleteFolder(ObjectId rootId,String path);

    public void createLink(ObjectId id, String path, Folder folder);

    public void deleteLink(ObjectId id, String path);

    public void updateLink(ObjectId id, String path, String name, String newName);
}
