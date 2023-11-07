package tech.ailef.snapadmin.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Logger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SnapAdminAuthTestProjectApplicationTests {
	
//	private static final String BASE_PACKAGE = "tech.ailef.snapadmin.test.models";
//
//	private static final Logger logger = Logger.getLogger(SnapAdminAuthTestProjectApplicationTests.class.getName());
	
	private static final String DBADMIN_ROOT_PATH = "/admin";
	
	private static final String BASE_URL = "http://localhost:8080" + DBADMIN_ROOT_PATH;
	
	/**
	 * Tests the the home page responds with a 302 redirect instead of
	 * serving the home page (the user is redirected to the login)
	 */
	@Test
	void testAuthRedirect() {
		assertDoesNotThrow(() -> {
			Response response = Jsoup.connect(BASE_URL).followRedirects(false).execute();
			assertEquals(302, response.statusCode());
		});
	}
	
}








