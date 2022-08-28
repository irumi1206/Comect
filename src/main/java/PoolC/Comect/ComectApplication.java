package PoolC.Comect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = ElasticsearchDataAutoConfiguration.class)
public class ComectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComectApplication.class, args);
	}

}
