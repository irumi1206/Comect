package PoolC.Comect.elasticUser.repository;

import PoolC.Comect.elasticUser.domain.ElasticUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticUserRepository extends ElasticsearchRepository<ElasticUser,String> {

    List<ElasticUser> findByNickname(String nickname);
    Optional<ElasticUser> findByUserId(String userId);
    void deleteByUserId(String userId);

}
