package tech.ailef.dbadmin.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tech.ailef.dbadmin.external.DbAdminAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(DbAdminAutoConfiguration.class)
public class SpringBootDbAdminTestProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDbAdminTestProjectApplication.class, args);
	}

}
