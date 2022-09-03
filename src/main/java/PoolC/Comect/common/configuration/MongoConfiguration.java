package PoolC.Comect.common.configuration;

import PoolC.Comect.image.repository.ImageRepository;
import PoolC.Comect.user.repository.UserRepository;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class Config extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        return new MongoClient(mongoClientURI);
    }

    @Primary
    @Bean("mongoMappingContext")
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    @Primary
    @Bean
    public MongoTransactionManager transactionManager(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory) throws Exception {
        return new MongoTransactionManager(mongoDbFactory);
    }

    @Primary
    @Bean("mongoDbFactory")
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }

    @Primary
    @Bean("mappingMongoConverter") //使用自定义的typeMapper去除写入mongodb时的“_class”字段
    public MappingMongoConverter mappingMongoConverter(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory,
                                                       @Qualifier("mongoMappingContext") MongoMappingContext mongoMappingContext) throws Exception {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Primary
    @Bean(name = "mongoTemplate")
    public MongoTemplate getMongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory,
                                          @Qualifier("mappingMongoConverter") MappingMongoConverter mappingMongoConverter) throws Exception {
        return new MongoTemplate(mongoDbFactory, mappingMongoConverter);
    }
}