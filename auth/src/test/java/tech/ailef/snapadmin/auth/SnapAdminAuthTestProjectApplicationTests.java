package tech.ailef.snapadmin.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import tech.ailef.snapadmin.auth.security.SecurityConfiguration;

@SpringBootTest
class SnapAdminAuthTestProjectApplicationTests {
	
//	private static final String BASE_PACKAGE = "tech.ailef.snapadmin.test.models";
//
//	private static final Logger logger = Logger.getLogger(SnapAdminAuthTestProjectApplicationTests.class.getName());
	
	private static final String DBADMIN_ROOT_PATH = "/admin";
	
	private static final String BASE_URL = "http://localhost:8080" + DBADMIN_ROOT_PATH;
	
	/**
	 * Utility function to perform login
	 * @param driver
	 */
	private void login(ChromeDriver driver, String username) {
		// Should be redirected to login
		driver.get(BASE_URL);
		
		// Fill in username and password
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys("password");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
	}
	
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
	
	/**
	 * Tests that the login works correctly by providing username "user"
	 * and password "password".
	 */
	@Test
	void testLogin() {
		ChromeDriver driver = new ChromeDriver();

		login(driver, "user");
		
		assertEquals("user", driver.findElement(By.id("current-user")).getText());
		
		driver.close();
	}
	
	/**
	 * Tests that after logging in as a user we are unable to create
	 * new items (as our {@link SecurityConfiguration} prevents this).
	 */
	@Test
	void testAuthorizationEditUser() {
		ChromeDriver driver = new ChromeDriver();

		login(driver, "user");
		
		// Should be redirected to home page
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.User/edit/1");
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		
		
		assertEquals(true, driver.findElement(By.tagName("body")).getText().contains("403 Forbidden"));
		
		driver.close();
	}
	
	/**
	 * Tests that logging in as admin we are able to edit.
	 */
	@Test
	void testAuthorizationEditAdmin() {
		ChromeDriver driver = new ChromeDriver();

		login(driver, "admin");
		
		// Should be redirected to home page
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.User/edit/2");
		driver.findElement(By.cssSelector("input[name='username']")).clear();
		driver.findElement(By.cssSelector("input[name='username']")).sendKeys("new_admin");
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		
		// Check that the username has actually been modified
		assertEquals(true, driver.findElement(By.tagName("body")).getText().contains("new_admin"));
		
		// Bring back the username to "admin" in case other tests rely on this avlue
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.User/edit/2");
		driver.findElement(By.cssSelector("input[name='username']")).clear();
		driver.findElement(By.cssSelector("input[name='username']")).sendKeys("admin");
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		
		// Check that new_admin has been removed
		assertEquals(false, driver.findElement(By.tagName("body")).getText().contains("new_admin"));
		
		driver.close();
	}
	
	/**
	 * Tests that the object is created with the correct field
	 * values or NULL if not provided (after logging in as ADMIN).
	 * @throws IOException
	 */
	@Test
	void testCreate() throws IOException {
		ChromeDriver driver = new ChromeDriver();
		
		login(driver, "admin");
		
		Map<String, String> productData = new HashMap<>();
		productData.put("id", "999");
		productData.put("name", "Test product");
		productData.put("price", "67.99");
		productData.put("eco_friendly", "true");
		
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.Product/create");
		
		for (String field : productData.keySet()) {
			driver.findElement(By.cssSelector("input[name=\"" + field + "\"]")).sendKeys(productData.get(field));
		}
		
		driver.findElement(By.cssSelector("textarea[name=\"description\"]")).sendKeys("Test description");
		
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		List<WebElement> rows = 
			driver.findElements(By.cssSelector("table")).get(0).findElements(By.cssSelector("tr"));
	
		int foundFields = 0;
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.cssSelector("td"));
			if (cols.isEmpty()) continue; // Skip header line, has <th>
			
			String fieldName = cols.get(1).getText();
			String fieldValue = cols.get(2).getText();
			
			if (fieldName.equals("price")) {
				assertEquals("$67.99", fieldValue);
				foundFields++;
			} else if (productData.containsKey(fieldName)) {
				assertEquals(productData.get(fieldName), fieldValue);
				foundFields++;
			} else if (fieldName.equals("description")) {
				assertEquals("Test description", fieldValue);
			} else {
				assertEquals("NULL", fieldValue);
			}
		}
		
		assertEquals(productData.size(), foundFields);
		
		driver.close();
	}
	
	/**
	 * Tests that the admin can correctly delete items.
	 * @throws InterruptedException
	 */
	@Test
	void testDelete() throws InterruptedException {
		ChromeDriver driver = new ChromeDriver();
		
		login(driver, "admin");
		
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.Product");
		driver.findElement(By.cssSelector("form.delete-form")).submit();
		
		Thread.sleep(100);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		driver.get(BASE_URL + "/model/tech.ailef.snapadmin.auth.models.Product/show/1");
		assertEquals(true, driver.findElement(By.tagName("body")).getText().contains("404 Error"));
		driver.close();
	}
	
	
	
}








