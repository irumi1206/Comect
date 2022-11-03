package PoolC.Comect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
//import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;

@SpringBootApplication
@EnableScheduling
public class ComectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComectApplication.class, args);
	}

}
