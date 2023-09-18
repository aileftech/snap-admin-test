package tech.ailef.dbadmin.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"tech.ailef.dbadmin"})
@EnableJpaRepositories(basePackages = {"tech.ailef.dbadmin"})
@EntityScan(basePackages = {"tech.ailef.dbadmin"})
public class SpringBootDbAdminTestProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDbAdminTestProjectApplication.class, args);
	}

}
