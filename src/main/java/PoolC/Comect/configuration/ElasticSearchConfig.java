package PoolC.Comect.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

public class ElasticSearchConfig extends AbstractElasticsearchConfiguration{

    @Value("${ELASTICSEARCH.HOST}")
    private String host;

    @Override
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchConverter elasticsearchConverter, RestHighLevelClient elasticsearchClient) {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration=ClientConfiguration.builder()
                .connectedTo(host.split(","))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }


}
