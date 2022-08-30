//package PoolC.Comect.common.configuration;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.mongo.MongoProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.MongoTransactionManager;
//import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
//import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//@Configuration
//public class MongoConfiguration extends AbstractMongoClientConfiguration {
//
//    @Autowired
//    private MongoProperties mongoProperties;
//
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return mongoProperties.getDatabase();
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        return MongoClients.create(mongoProperties.getUri());
//    }
//}
