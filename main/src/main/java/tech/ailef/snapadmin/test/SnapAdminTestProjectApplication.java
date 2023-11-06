package tech.ailef.snapadmin.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tech.ailef.snapadmin.external.SnapAdminAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(SnapAdminAutoConfiguration.class)
public class SnapAdminTestProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnapAdminTestProjectApplication.class, args);
	}

}
