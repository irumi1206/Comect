package PoolC.Comect.elasticFolder.repository;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;

import java.util.List;

public interface CustomElasticLinkRepository {

    public List<ElasticLink> searchLinkMe(String id, String keyword);
    public List<ElasticLink> searchLink(List<String> ids, String keyword);
    public List<ElasticLink> searchExcludeLink(List<String> ids, String keyword);

    public void delete(String ownerId, String path,String id);
    public void update(String ownerId, String path, String id, String newId,String newName,String keywords,String isPublic);
    public void move(String ownerId, String originalPath, String id, String destinationPath);

    public void deleteFolder(String ownerId, String path);
    public void updateFolder(String ownerId, String path,String newName);
    public void moveFolder(String ownerId, String originalPath, String destinationPath);
}
