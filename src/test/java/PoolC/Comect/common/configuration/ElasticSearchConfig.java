package PoolC.Comect.common.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private static final GenericContainer container;

    static {
        container = new GenericContainer(
                new ImageFromDockerfile()
                        .withDockerfileFromBuilder(builder -> {
                            builder
                                    .from("docker.elastic.co/elasticsearch/elasticsearch:7.15.2")
                                    .run("bin/elasticsearch-plugin install analysis-nori")
                                    .build();
                        })
        ).withExposedPorts(9200,9300)
                .withEnv("discovery.type","single-node");

        container.start();
    }

    @Override
    //@Primary
    public RestHighLevelClient elasticsearchClient() {
        System.out.println("test!!!!!!");
        String hostAddress = new StringBuilder()
                .append(container.getHost())
                .append(":")
                .append(container.getMappedPort(9200))
                .toString();

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAddress)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean(name = { "elasticsearchOperations", "elasticsearchTemplate" })
    //@Primary
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchConverter elasticsearchConverter,
                                                           RestHighLevelClient elasticsearchClient) {

        ElasticsearchRestTemplate template = new ElasticsearchRestTemplate(elasticsearchClient, elasticsearchConverter);
        template.setRefreshPolicy(refreshPolicy());

        return template;
    }
}
