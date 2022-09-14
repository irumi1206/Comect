package PoolC.Comect.elasticUser.service;

import PoolC.Comect.elasticUser.repository.ElasticUserRepository;
import PoolC.Comect.elasticUser.domain.ElasticUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticUserService {

    private final ElasticUserRepository elasticUserRepository;

    public List<String> searchUser(String keyword){
        return elasticUserRepository.findByNickname(keyword).stream().map(ElasticUser::getNickname).collect(Collectors.toList());
    }

}
