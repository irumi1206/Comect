package PoolC.Comect.common.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableReactiveMongoAuditing
@EnableTransactionManagement
@Slf4j
public class MongoConfiguration {

    @Value("${spring.data.mongodb.url}")
    private String mongoUrl;

    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create(mongoUrl);
    }

    @Bean
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory){
        return new ReactiveMongoTransactionManager(factory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveMongoTransactionManager transactionManager){
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(){
        return new SimpleReactiveMongoDatabaseFactory(mongoClient(),"mongotest");
    }
}
