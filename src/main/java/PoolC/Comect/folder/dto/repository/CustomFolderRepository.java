package PoolC.Comect.folder.repository;

import PoolC.Comect.folder.domain.Folder;
import PoolC.Comect.folder.domain.Link;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFolderRepository {

    public boolean checkPathFolder(ObjectId rootId,String path);
    public boolean checkPathLink(ObjectId rootId,String path);
    public void folderCreate(ObjectId rootId,String path, Folder folder);
    public Folder folderRead(ObjectId rootId, String path);
    public void folderUpdate(ObjectId rootId,String path,String folderName);
    public void folderDelete(ObjectId rootId,String path);
    public void linkCreate(ObjectId rootId,String path,Link link);
    public Link linkRead(ObjectId rootId, String path, ObjectId id);
    public void linkUpdate(ObjectId rootId,String path,ObjectId id,Link link);
    public void linkDelete(ObjectId rootId,String path,ObjectId id);
}
