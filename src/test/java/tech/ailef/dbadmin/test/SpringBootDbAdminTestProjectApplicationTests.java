package tech.ailef.dbadmin.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootDbAdminTestProjectApplicationTests {
	
	private static final String BASE_PACKAGE = "tech.ailef.dbadmin.test.models";

	private static final Logger logger = Logger.getLogger(SpringBootDbAdminTestProjectApplicationTests.class.getName());
	
	private static final String DBADMIN_ROOT_PATH = "/admin";
	
	private static final String BASE_URL = "http://localhost:8080" + DBADMIN_ROOT_PATH;
	
	private static final String[] CLASSES = {
		"Cart",
		"CartItem",
		"Product",
		"User",
		"OrderLine",
		"Order",
		"Category",
		"Tag"
	};
	
	private static final String[] TEST_200_OK_URLS = {
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Cart/show/1",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/show/1",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Order/show/42",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/show/3ccff81d-9f57-44b4-b414-5dc8bed05a28",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product?query=apple",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product?query=apple&page=3",
		BASE_URL + "/model/tech.ailef.dbadmin.test.models.Order?query=d7"
	};

	@Test
	void contextLoads() {
	}

	/**
	 * This checks a list of hardcoded pages of different types (list, show, create, etc...)
	 * in order to see if they all reply with 200 OK. This will NOT check the actual content
	 * of the page for correctness.
	 * @throws IOException 
	 */
	@Test
	void basicHttpOkResponse() throws IOException {
		logger.info("Testing 200 OK response");
		for (String klass : CLASSES) {
			String path = BASE_PACKAGE + "." + klass;

			// Index page
			Response resp = Jsoup.connect(BASE_URL + "/model/" + path).execute();
			assertEquals(200, resp.statusCode());
			
			// Schema page
			resp = Jsoup.connect(BASE_URL + "/model/" + path + "/schema").execute();
			assertEquals(200, resp.statusCode());
			
			// Create page
			resp = Jsoup.connect(BASE_URL + "/model/" + path + "/create").execute();
			assertEquals(200, resp.statusCode());
		}
		
		for (String url : TEST_200_OK_URLS) {
			logger.info("Testing 200 OK for " + url);
			Response resp = Jsoup.connect(url).execute();
			assertEquals(200, resp.statusCode());
		}
	}
	
	@Test
	void testShowPage() throws IOException {
		logger.info("Testing show page");
		Response resp = Jsoup.connect(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/show/1").execute();
		String content = resp.body();
		Document document = Jsoup.parse(content);
		
		// Test title
		String titleText = document.selectFirst("h1").text();
		assertTrue(titleText.endsWith("iPhone 12 $699.99"));

		// Test rows of the show table
		Element e1 = document.selectFirst("table.show-table tr:nth-child(3) td:nth-child(2)");
		assertEquals("name", e1.text());
		Element e2 = document.selectFirst("table.show-table tr:nth-child(3) td:nth-child(3)");
		assertEquals("iPhone 12", e2.text());
		Element e3 = document.selectFirst("table.show-table tr:nth-child(3) td:nth-child(4)");
		assertEquals("STRING", e3.text());
		Element e4 = document.selectFirst("table.show-table tr:nth-child(4) td:nth-child(2)");
		assertEquals("price", e4.text());
		Element e5 = document.selectFirst("table.show-table tr:nth-child(4) td:nth-child(3)");
		assertEquals("$699.99", e5.text());
		Element e6 = document.selectFirst("table.show-table tr:nth-child(4) td:nth-child(4)");
		assertEquals("DOUBLE", e6.text());
		
		// Test the one to many table
		Element secondTable = document.select("table").get(2);
		Elements cols = secondTable.select("td");
		assertEquals("58", cols.get(1).text());
		assertEquals("42 42", cols.get(2).text());
		assertEquals("578.89", cols.get(3).text());
		assertEquals("1 iPhone 12 $699.99", cols.get(4).text());
		assertEquals("1", cols.get(5).text());
	}
	
	
	@Test
	void testListPage() throws IOException {
		Response resp = 
			Jsoup.connect(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User?sortKey=id&sortOrder=DESC").execute();
	
		Document document = Jsoup.parse(resp.body());
		Element table = document.selectFirst("table");
		Elements rows = table.select("tr");
		
		Elements cols = rows.get(0).select("th");
		assertEquals("id STRING", cols.get(2).text());
		assertEquals("cart_id LONG", cols.get(3).text());
		assertEquals("name STRING", cols.get(4).text());
		assertEquals("number_of_orders COMPUTED", cols.get(5).text());
		
		cols = rows.get(1).select("td");
		assertEquals("ffd5500e-1231-48e2-8384-3dc15fc7ed90", cols.get(2).text());
		assertEquals("5 5", cols.get(3).text());
		assertEquals("Oliver Williams", cols.get(4).text());
		assertEquals("67.0", cols.get(5).text());
	}
	
	/**
	 * Tests that the object is created with the correct field
	 * values or NULL if not provided 
	 * @throws IOException
	 */
	@Test
	void testCreate() throws IOException {
		ChromeDriver driver = new ChromeDriver();
		
		Map<String, String> productData = new HashMap<>();
		productData.put("id", "999");
		productData.put("name", "Test product");
		productData.put("price", "67.99");
		productData.put("eco_friendly", "true");
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/create");
		
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
	
	@Test
	void testEditPageFrontEnd() throws IOException {
		Document document = 
			Jsoup.parse(
				Jsoup.connect(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/edit/3ccff81d-9f57-44b4-b414-5dc8bed05a28")
				     .execute().body()
			);
		
		String id = document.selectFirst("input[name=\"id\"]").val();
		assertEquals("3ccff81d-9f57-44b4-b414-5dc8bed05a28", id);
		
		String cartId = document.selectFirst("input[name=\"cart_id\"]").val();
		assertEquals("2", cartId);
		
		String name = document.selectFirst("input[name=\"name\"]").val();
		assertEquals("Ethan Anderson", name);
	}
	
	@Test
	void testEdit() throws IOException {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/edit/24e137ba-d5b5-4ca7-aad1-5359463e0a53");
		WebElement nameElement = driver.findElement(By.cssSelector("input[name=\"name\"]"));
		nameElement.sendKeys("a");
		
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Map<String, String> userData = new HashMap<>();
		userData.put("id", "24e137ba-d5b5-4ca7-aad1-5359463e0a53");
		userData.put("cart_id", "9\n9");
		userData.put("name", "Olivia Evansa");
		userData.put("number_of_orders", "46.0");
		
		List<WebElement> rows = 
			driver.findElements(By.cssSelector("table")).get(0).findElements(By.cssSelector("tr"));
		
		int foundFields = 0;
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.cssSelector("td"));
			if (cols.isEmpty()) continue; // Skip header line, has <th>
			
			String fieldName = cols.get(1).getText();
			String fieldValue = cols.get(2).getText();
			
			if (userData.containsKey(fieldName)) {
				foundFields++;
				assertEquals(userData.get(fieldName), fieldValue);
			}

		}

		assertEquals(userData.size(), foundFields);
		
		driver.close();
	}
	
	/**
	 * Tests that when a user creates an object but gets an error,
	 * the other fields the he filled are still present with the
	 * correct value in the new page. 
	 */
	@Test
	void testCreateRememberFields() {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Category/create");
		
		driver.findElement(By.cssSelector("input[name=\"name\"]")).sendKeys("Test name");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		String value = driver.findElement(By.cssSelector("input[name=\"name\"]")).getAttribute("value");
		assertEquals("Test name", value);
		
		driver.close();
	}
	
	/**
	 * Tests the download controller. It downloads the binary blob associated
	 * to the Product with id 1 and checks if the downloaded content is correct.
	 * @throws IOException 
	 */
	@Test
	void testDownload() throws IOException {
		String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor "
				+ "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation"
				+ " ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
				+ " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat"
				+ " cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		String body = 
			Jsoup.connect(BASE_URL + "/download/tech.ailef.dbadmin.test.models.Product/thumbnailImage/1")
				 .execute().body();
		assertEquals(body.trim(), loremIpsum);
		
		// Test 404 with wrong ID
		try {
			body = 
				Jsoup.connect(BASE_URL + "/download/tech.ailef.dbadmin.test.models.Product/thumbnailImage/3")
					 .execute().body();
			throw new RuntimeException("Request should've generated 404 error");
		} catch (HttpStatusException e) {
			assertEquals(404, e.getStatusCode());
		}
		
		// Test 404 with wrong field name
		try {
			body = 
				Jsoup.connect(BASE_URL + "/download/tech.ailef.dbadmin.test.models.Product/missingField/3")
					 .execute().body();
		} catch (HttpStatusException e) {
			assertEquals(404, e.getStatusCode());
		}
		
	}
	
	@Test
	void testFacetedSearch() throws IOException {
		Document document = Jsoup.parse(
			Jsoup.connect(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product?"
					+ "page=1&pageSize=50&query=&filter_field=description&filter_op=contains&filter_value=128")
				.execute().body()
		);
		
		Elements filters = document.select("span.active-filter");
		assertEquals(1, filters.size());
		assertEquals("'description' Contains '128'", filters.get(0).text());
		
		// Test url with remove filter parameters
		document = Jsoup.parse(
			Jsoup.connect(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product?"
				+ "page=1&pageSize=50&query=&filter_field=description&filter_op=contains&filter_value=128&"
				+ "remove_field=description&remove_op=contains&remove_value=128")
				.execute().body()
		);
		filters = document.select("span.active-filter");
		assertEquals(0, filters.size());
		
			
	}

	@Test
	void testLinks() throws InterruptedException, IOException {
		ChromeDriver driver = new ChromeDriver();
		
		// Test links in list page
		for (String klass : CLASSES) {
			driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models." + klass);
			List<WebElement> links = driver.findElements(By.cssSelector("a"));
			List<String> urls = links.stream().map(l -> l.getAttribute("href"))
						.limit(25)
						.collect(Collectors.toList());
	
			for (String url : urls) {
				logger.info("Testing link URL (from list page): " + url);
				driver.get(url);
				assertFalse(driver.findElement(By.tagName("body")).getText().contains("Whitelabel Error Page"));
			}
		}
		
		// Test links in show page
		for (String klass : CLASSES) {
			String testId = klass.equals("User") ? "24e137ba-d5b5-4ca7-aad1-5359463e0a53" : "1";
			String editUrl = BASE_URL + "/model/" + BASE_PACKAGE +  "." + klass + "/show/" + testId;
			
			driver.get(editUrl);
			List<WebElement> links = driver.findElements(By.tagName("a"));
			List<String> urls = links.stream().map(l -> l.getAttribute("href"))
						.limit(25)
						.collect(Collectors.toList());
	
			for (String url : urls) {
				logger.info("Testing link URL (from show page): " + url);
				driver.get(url);
				assertFalse(driver.findElement(By.tagName("body")).getText().contains("Whitelabel Error Page"));
			}
	
		}
		driver.close();
		
	}


	@Test
	void testAutocomplete() throws IOException, InterruptedException {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.OrderLine/edit/1");
		// Send the string "nok" and check that Nokia results appear
		driver.findElement(By.cssSelector("input[name=\"product_id\"]")).clear();
		driver.findElement(By.cssSelector("input[name=\"product_id\"]")).sendKeys("nok");
		Thread.sleep(200);
		driver.findElement(By.cssSelector("input[name=\"product_id\"]")).sendKeys("i");
		Thread.sleep(500); // Need to give time to the autocomplete to load
		
		WebElement autocomplete = driver.findElements(By.cssSelector("div.suggestions.d-block")).get(0);
		List<WebElement> suggestions = autocomplete.findElements(By.cssSelector(".suggestion"));
		assertEquals("Nokia 8.3 $699.0", suggestions.get(0).findElement(By.tagName("p")).getText());
		assertEquals("Nokia 9.3 $799.0", suggestions.get(1).findElement(By.tagName("p")).getText());
		assertEquals("Nokia 7.2 $349.0", suggestions.get(2).findElement(By.tagName("p")).getText());
		
		// Click a field and check that the suggestion to type appears
		driver.findElement(By.cssSelector("input[name=\"order_id\"]")).click();
		Thread.sleep(500); // Need to give time to the autocomplete to load
		autocomplete = driver.findElements(By.cssSelector("div.suggestions.d-block")).get(0);
		assertEquals("Enter a valid ID or start typing for suggestions", autocomplete.getText());

		driver.close();
	}

	@Test
	void testActionLogs() throws IOException {
		// Edit product id 41 and then check that the edit appears in the logs
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/edit/41");
		driver.findElement(By.cssSelector("#__id_name")).sendKeys("A");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.get(BASE_URL + "/logs?actionType=Any&table=Any&itemId=41");
		
		WebElement row = driver.findElements(By.cssSelector("tr")).get(1);
		List<WebElement> cols = row.findElements(By.cssSelector("td"));
		assertEquals("EDIT", cols.get(0).getText());
		assertEquals("products", cols.get(1).getText());
		assertEquals("41", cols.get(2).getText());
		
		driver.close();
	}
	
	@Test
	void testEmptyCreateEdit() throws InterruptedException, IOException {
		ChromeDriver driver = new ChromeDriver();

		driver.get(BASE_URL);
		
		for (String klass : CLASSES) {
			String createUrl = BASE_URL + "/model/" + BASE_PACKAGE +  "." + klass + "/create";
			logger.info("Testing empty create for " + createUrl);
			
			driver.get(createUrl);
			driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			assertFalse(driver.findElement(By.tagName("body")).getText().contains("Whitelabel Error Page"));
		}
		
		for (String klass : CLASSES) {
			String testId = klass.equals("User") ? "24e137ba-d5b5-4ca7-aad1-5359463e0a53" : "1";
			String editUrl = BASE_URL + "/model/" + BASE_PACKAGE +  "." + klass + "/edit/" + testId;
			logger.info("Testing no-modifications edit for " + editUrl);
			
			driver.get(editUrl);
			driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			assertFalse(driver.findElement(By.tagName("body")).getText().contains("Whitelabel Error Page"));
		}
		
		logger.info("Testing no changes applied after no-modification edits");
		for (String klass : CLASSES) {
			String testId = klass.equals("User") ? "24e137ba-d5b5-4ca7-aad1-5359463e0a53" : "1";
			String showUrl = BASE_URL + "/model/" + BASE_PACKAGE +  "." + klass + "/show/" + testId;
			
			if (klass.endsWith("Product")) {
				String[] colValues = 
					{"1", "iPhone 12", "$699.99", "2022-07-01T10:00:01", "Apple iPhone 12 with 64GB Memory", "true", "NULL", "Download"};
				logger.info("Testing: " + showUrl);

				Document document = Jsoup.parse(Jsoup.connect(showUrl).execute().body());
				
				for (int i = 0; i < colValues.length; i++) {
					Element row = document.selectFirst("table.show-table tr:nth-child(" + (i + 2) + ")");
					String colText = row.selectFirst("td:nth-child(3)").text();
					assertEquals(colValues[i], colText);
				}
			}
		}
		driver.close();
	}

	@Test
	void testSearch() {
		ChromeDriver driver = new ChromeDriver();

		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product?query=App&page=1&pageSize=50");
		List<WebElement> elements = driver.findElements(By.cssSelector("tr"));
		assertEquals(6, elements.size());
		
		driver.close();
	}
	
	/**
	 * Test that the hidden "password" column for the User class
	 * is not shown when listing the users or in the detail page.
	 * Check, instead, that it's correctly shown in create/edit,
	 * because the field is not nullable and thus required.
	 */
	@Test
	void testHiddenColumn() {
		ChromeDriver driver = new ChromeDriver();
		
		// List page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User");
		List<WebElement> ths = driver.findElements(By.cssSelector("th"));
		for (WebElement th : ths) {
			assertEquals(true, !th.getText().contains("password"));
		}
		
		// Detail page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/show/24e137ba-d5b5-4ca7-aad1-5359463e0a53");
		List<WebElement> tds = driver.findElements(By.cssSelector("td"));
		for (WebElement td : tds) {
			assertEquals(true, !td.getText().contains("password"));
		}
		
		// Test edit page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/edit/24e137ba-d5b5-4ca7-aad1-5359463e0a53");
		
		WebElement element = driver.findElement(By.id("__id_password"));
		assertNotNull(element);

		// Test create page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.User/create");
		element = driver.findElement(By.id("__id_password"));
		assertNotNull(element);
		
		driver.close();
	}
	
	/**
	 * Tests that the edit and create form correctly display read only fields,
	 * that is normally on create and disabled on edit.
	 */
	@Test
	void testReadOnlyFrontEnd() {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/edit/31");
		WebElement createdAtInput = driver.findElement(By.cssSelector("input[name=\"created_at\"]"));
		String cssClass = createdAtInput.getAttribute("class");
		assertTrue(cssClass.contains("disable"));
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/create");
		createdAtInput = driver.findElement(By.cssSelector("input[name=\"created_at\"]"));
		cssClass = createdAtInput.getAttribute("class");
		assertFalse(cssClass.contains("disable"));
		
		driver.close();
	}
	
	/**
	 * Tests that the repository implementation doesn't apply changes to read only fields.
	 * Check that after sending an edit request through the web UI the value doesn't change.
	 */
	@Test
	void testReadOnlyBackEnd() {
		ChromeDriver driver = new ChromeDriver();

        JavascriptExecutor js = (JavascriptExecutor) driver;
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/edit/7");
		WebElement createdAtInput = driver.findElement(By.cssSelector("input[name=\"created_at\"]"));
		js.executeScript("arguments[0].value = '2052-07-01T10:00:01';", createdAtInput);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		// Test that after the edit the date has not changed
		WebElement ele = driver.findElement(By.cssSelector("tr:nth-child(5) td:nth-child(3)"));
		assertEquals("2022-07-01T10:00:01", ele.getText());
		
		driver.close();
	}
	
	/**
	 * The test projects contains some fields with an unsupported type.
	 * Check that the correct visual clues and error messages are present
	 * where appropriate.
	 */
	@Test
	void testMappingErrors() {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/");
		
		WebElement secondTable = driver.findElements(By.tagName("table")).get(1);
		WebElement warningCol = secondTable.findElement(By.cssSelector("td.warning-col"));
		
		// Check that a NoSuchElementException is not thrown,
		// i.e. the link is present to the schema page
		assertDoesNotThrow(() -> { 
			warningCol.findElement(By.tagName("a"));
		}, "<a> tag not found in .warning-col element");
		
		// Go to the schema page (if link is correct)
		warningCol.findElement(By.tagName("a")).click();
		
		List<WebElement> titles = driver.findElements(By.tagName("h3"));
		WebElement errorSectionTitle = 
			titles.stream().filter(t -> t.getText().equals("Errors")).findFirst().orElse(null);
		
		assertNotNull(errorSectionTitle);

		assertDoesNotThrow(() -> {
			driver.findElement(By.cssSelector("ul.mapping-errors"));
		}, "List of mapping errors not found");
		
		String text = driver.findElement(By.cssSelector("ul.mapping-errors li")).getText();
		assertEquals(
			"The class contains the field `createdAt` of type `Calendar`, which is not supported", 
			text.trim()
		);
		
		driver.close();
	}
	
	/**
	 * Tests that the DELETE options is actually disabled both on the 
	 * front end (additional CSS class applied) and on the back end
	 * (if DELETE request sent anyway, it fails).
	 */
	@Test
	void testDisableDelete() {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.OrderLine");
		List<WebElement> checks = driver.findElements(By.cssSelector("input[type=\"checkbox\"]"));
		for (WebElement c : checks) {
			assertTrue(c.getAttribute("class").contains("disable"));
		}
		
		List<WebElement> forms = driver.findElements(By.cssSelector("form.delete-form"));
		for (WebElement f : forms) {
			WebElement button = f.findElement(By.tagName("button"));
			assertTrue(button.getAttribute("class").contains("disable"));
		}
		
		// Test also in the Product show page where OrderLines are
		// displayed because of the OneToMany relationship
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Product/show/1");
		WebElement orderLineTable = driver.findElements(By.tagName("table")).get(2);
		forms = orderLineTable.findElements(By.cssSelector("form.delete-form"));
		for (WebElement f : forms) {
			WebElement button = f.findElement(By.tagName("button"));
			assertTrue(button.getAttribute("class").contains("disable"));
		}
		
		// Even if disabled, submit the form anyway via Javascript and check
		// that we are redirected to the correct error
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].submit()", forms.get(0));
		
		List<WebElement> alerts = driver.findElements(By.cssSelector(".alert-danger"));
		assertFalse(alerts.isEmpty());
		
		WebElement alert = alerts.get(0);
		String errorMessage = alert.findElement(By.tagName("p")).getText();
		assertEquals("DELETE operations have been disabled on this table.", errorMessage.trim());
		
		driver.close();
	}
	
	/**
	 * Tests that the CREATE option is actually disabled both on the 
	 * front end (additional CSS class applied) and that the create page
	 * is not served (still TODO: check POST request).
	 */
	@Test
	void testDisableCreate() {
		ChromeDriver driver = new ChromeDriver();

		// List page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.additional.InventoryItem");
		WebElement createLink = driver.findElement(By.cssSelector("h3.create-button a"));
		assertTrue(createLink.getAttribute("class").contains("disable"));
		assertThrows(ElementClickInterceptedException.class, () -> createLink.click());
		
		// Schema page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.additional.InventoryItem/schema");
		WebElement otherCreateLink = driver.findElement(By.cssSelector("h3.create-button a"));
		assertThrows(ElementClickInterceptedException.class, () -> otherCreateLink.click());
		
		assertTrue(otherCreateLink.getAttribute("class").contains("disable"));
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.additional.InventoryItem/create");
		WebElement alertTitle = driver.findElement(By.cssSelector(".alert-danger h6"));
		assertEquals("Unauthorized", alertTitle.getText().trim());
		
		driver.close();
	}
	
	/**
	 * Tests that the EDIT option is actually disabled both on the 
	 * front end (additional CSS class applied) and that the edit page
	 * is not served (still TODO: check POST request).
	 */
	@Test
	void testDisableEdit() {
		ChromeDriver driver = new ChromeDriver();
		
		// List page
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.additional.UneditableExample");
		WebElement createLink = driver.findElement(By.cssSelector(".row-icons a"));
		assertTrue(createLink.getAttribute("class").contains("disable"));
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.additional.UneditableExample/edit/1");
		WebElement alertTitle = driver.findElement(By.cssSelector(".alert-danger h6"));
		assertEquals("Unauthorized", alertTitle.getText().trim());
		
		driver.close();
	}
	
	/**
	 * Sends an empty form on an item that has validation on its fields.
	 * Checks that the resulting page shows the validation errors.
	 */
	@Test
	void testValidationOnCreate() {
		ChromeDriver driver = new ChromeDriver();

		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.ValidatedItem/create");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		List<WebElement> invalidDivs = driver.findElements(By.cssSelector("div.invalid"));
		// Check that all 3 fields have the invalid class
		assertEquals(3, invalidDivs.size());
		
		for (WebElement fieldDiv : invalidDivs) {
			WebElement findElement = fieldDiv.findElement(By.cssSelector(".text-red"));
			assertEquals("must not be null", findElement.getText());
		}
		
		driver.close();
	}
	
	/**
	 * Checks that the validation works on edit pages
	 */
	@Test
	void testValidationOnEdit() {
		ChromeDriver driver = new ChromeDriver();

		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.ValidatedItem/edit/1957447a-a2fe-4203-a055-197235f5edb9");
		
		// Set a short name that should fail validation
		driver.findElement(By.cssSelector("input[name=\"name\"]")).clear();
		driver.findElement(By.cssSelector("input[name=\"name\"]")).sendKeys("NO");
		
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		List<WebElement> invalidDivs = driver.findElements(By.cssSelector("div.invalid"));
		assertEquals(1, invalidDivs.size());
		
		for (WebElement fieldDiv : invalidDivs) {
			WebElement findElement = fieldDiv.findElement(By.cssSelector(".text-red"));
			assertEquals("Name must be 5-10 characters", findElement.getText());
		}
		
		driver.close();
	}
	
	/**
	 * Tests that we can correctly add entries to a many to many relationship
	 * on the edit page, using the autocomplete form.
	 * @throws InterruptedException
	 */
	@Test
	void testSetManyToMany() throws InterruptedException {
		ChromeDriver driver = new ChromeDriver();
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.Category/edit/2");
		
		driver.findElement(By.cssSelector("input[name=\"products[]\"]")).sendKeys("iPhone 12");
		Thread.sleep(200);
		driver.findElement(By.cssSelector(".suggestions .suggestion")).click();
		driver.findElement(By.tagName("body")).click();
		Thread.sleep(200);
		driver.findElement(By.cssSelector("input[name=\"products[]\"]")).sendKeys("Samsung Galaxy S21");
		Thread.sleep(200);
		driver.findElement(By.cssSelector(".suggestions .suggestion")).click();
		driver.findElement(By.tagName("body")).click();
		Thread.sleep(200);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		WebElement table = driver.findElements(By.tagName("table")).get(1);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement nameColumn = rows.get(1).findElements(By.tagName("td")).get(2);
		assertEquals("iPhone 12", nameColumn.getText());
		
		nameColumn = rows.get(2).findElements(By.tagName("td")).get(2);
		assertEquals("Samsung Galaxy S21", nameColumn.getText());
		
		driver.close();
	}
	
	@Test
	void testDelete() throws InterruptedException {
		ChromeDriver driver = new ChromeDriver();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.CartItem");
		driver.findElement(By.cssSelector("form.delete-form")).submit();
		
		Thread.sleep(100);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		driver.get(BASE_URL + "/model/tech.ailef.dbadmin.test.models.CartItem/show/1");
		assertEquals(true, driver.findElement(By.tagName("body")).getText().contains("404 Error"));
		driver.close();
	}
}








