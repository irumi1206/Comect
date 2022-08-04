package PoolC.Comect;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "linky";
    }
    @Override
    public MongoClient reactiveMongoClient(){
        return MongoClients.create("mongodb://localhost:27017");
    }
}
