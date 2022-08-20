package PoolC.Comect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"PoolC.Comect.data"})
public class ComectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComectApplication.class, args);
	}

}
