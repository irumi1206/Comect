package PoolC.Comect.elasticFolder.service;

import PoolC.Comect.elasticFolder.domain.ElasticFolder;
import PoolC.Comect.elasticFolder.domain.ElasticLink;
import PoolC.Comect.elasticFolder.repository.ElasticFolderRepository;
import PoolC.Comect.elasticFolder.repository.ElasticLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticFolderRepository elasticFolderRepository;
    private final ElasticLinkRepository elasticLinkRepository;

    public List<ElasticFolder> searchFolder(List<String> ids,String keyword){
        List<ElasticFolder> elasticFolderList=elasticFolderRepository.searchFolder(ids,keyword);
        return elasticFolderList;
    }

    public List<ElasticFolder> searchExcludeFolder(List<String> ids,String keyword){
        List<ElasticFolder> elasticFolderList=elasticFolderRepository.searchExcludeFolder(ids,keyword);
        return elasticFolderList;
    }

    public List<ElasticLink> searchLinkMe(String id,String keyword){
        List<ElasticLink> elasticLinkList=elasticLinkRepository.searchLinkMe(id,keyword);
        return elasticLinkList;
    }

    public List<ElasticLink> searchLink(List<String> ids,String keyword){
        List<ElasticLink> elasticLinkList=elasticLinkRepository.searchLink(ids,keyword);
        return elasticLinkList;
    }

    public List<ElasticLink> searchExcludeLink(List<String> ids, String keyword){
        List<ElasticLink> elasticLinkList=elasticLinkRepository.searchExcludeLink(ids,keyword);
        return elasticLinkList;
    }
}
