package tech.ailef.dbadmin.test;

import org.springframework.context.annotation.Configuration;

import tech.ailef.dbadmin.annotations.DbAdminAppConfiguration;
import tech.ailef.dbadmin.annotations.DbAdminConfiguration;

@DbAdminConfiguration
@Configuration
public class TestConfiguration implements DbAdminAppConfiguration {

	@Override
	public String getModelsPackage() {
		return "tech.ailef.dbadmin.test.models";
	}
}
