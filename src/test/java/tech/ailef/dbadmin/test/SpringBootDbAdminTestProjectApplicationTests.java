package tech.ailef.dbadmin.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootDbAdminTestProjectApplicationTests {
	
	private final String BASE_PACKAGE = "tech.ailef.dbadmin.test.models";

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private final String BASE_HOST = "http://localhost:8080";
	
	private final String[] CLASSES = {
		"Cart",
		"CartItem",
		"Product",
		"User",
		"OrderLine",
		"Order",
		"Category",
		"Tag"
	};
	
	private final String[] TEST_200_OK_URLS = {
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Cart/show/1",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Product/show/1",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Order/show/42",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.User/show/3ccff81d-9f57-44b4-b414-5dc8bed05a28",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Product?query=apple",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Product?query=apple&page=3",
		BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Order?query=d7"
	};

	@Test
	void contextLoads() {
	}

	/**
	 * This checks a list of hardcoded pages of different types (list, show, create, etc...)
	 * in order to see if they all reply with 200 OK. This will NOT check the actual content
	 * of the page for correctness.
	 */
	@Test
	void basicHttpOkResponse() {
		logger.info("Testing 200 OK response");
		try {
			for (String klass : CLASSES) {
				String path = BASE_PACKAGE + "." + klass;

				// Index page
				Response resp = Jsoup.connect(BASE_HOST + "/dbadmin/model/" + path).execute();
				assertEquals(200, resp.statusCode());
				
				// Schema page
				resp = Jsoup.connect(BASE_HOST + "/dbadmin/model/" + path + "/schema").execute();
				assertEquals(200, resp.statusCode());
				
				// Create page
				resp = Jsoup.connect(BASE_HOST + "/dbadmin/model/" + path + "/create").execute();
				assertEquals(200, resp.statusCode());
			}
			
			for (String url : TEST_200_OK_URLS) {
				Response resp = Jsoup.connect(url).execute();
				assertEquals(200, resp.statusCode());
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void testShowPage() {
		logger.info("Testing show page");
		try {
			Response resp = Jsoup.connect(BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.Product/show/1").execute();
			String content = resp.body();
			Document document = Jsoup.parse(content);
			
			// Test title
			String titleText = document.selectFirst("h1").text();
			assertTrue(titleText.endsWith("iPhone 12 $699.99"));

			// Test rows of the show table
			Element e1 = document.selectFirst("table.show-table tr:nth-child(5) td:nth-child(2)");
			assertEquals("name", e1.text());
			Element e2 = document.selectFirst("table.show-table tr:nth-child(5) td:nth-child(3)");
			assertEquals("iPhone 12", e2.text());
			Element e3 = document.selectFirst("table.show-table tr:nth-child(5) td:nth-child(4)");
			assertEquals("STRING", e3.text());
			Element e4 = document.selectFirst("table.show-table tr:nth-child(6) td:nth-child(2)");
			assertEquals("price", e4.text());
			Element e5 = document.selectFirst("table.show-table tr:nth-child(6) td:nth-child(3)");
			assertEquals("$699.99", e5.text());
			Element e6 = document.selectFirst("table.show-table tr:nth-child(6) td:nth-child(4)");
			assertEquals("DOUBLE", e6.text());
			
			// Test the one to many table
			Element secondTable = document.select("table").get(2);
			Elements cols = secondTable.select("td");
			assertEquals("58", cols.get(0).text());
			assertEquals("42 42", cols.get(1).text());
			assertEquals("578.89", cols.get(2).text());
			assertEquals("1 iPhone 12 $699.99", cols.get(3).text());
			assertEquals("1", cols.get(4).text());
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Test
	void testListPage() {
		try {
			Response resp = 
				Jsoup.connect(BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.User").execute();
		
			Document document = Jsoup.parse(resp.body());
			Element table = document.selectFirst("table");
			Elements rows = table.select("tr");
			
			Elements cols = rows.get(0).select("th");
			assertEquals("id STRING", cols.get(1).text());
			assertEquals("cart_id LONG", cols.get(2).text());
			assertEquals("name STRING", cols.get(3).text());
			assertEquals("total_spent COMPUTED", cols.get(4).text());
			
			cols = rows.get(1).select("td");
			assertEquals("d7558967-c177-40f1-8360-25c7806329df", cols.get(1).text());
			assertEquals("1 1", cols.get(2).text());
			assertEquals("Benjamin Mitchell", cols.get(3).text());
			assertEquals("40520.5", cols.get(4).text());
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void testEditPage() {
		try {
			Document document = 
				Jsoup.parse(
					Jsoup.connect(BASE_HOST + "/dbadmin/model/tech.ailef.dbadmin.test.models.User/edit/3ccff81d-9f57-44b4-b414-5dc8bed05a28")
					     .execute().body()
				);
			
			String id = document.selectFirst("input[name=\"id\"]").val();
			assertEquals("3ccff81d-9f57-44b4-b414-5dc8bed05a28", id);
			
			String cartId = document.selectFirst("input[name=\"cart_id\"]").val();
			assertEquals("2", cartId);
			
			String name = document.selectFirst("input[name=\"name\"]").val();
			assertEquals("Ethan Anderson", name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Tests the download controller. It downloads the binary blob associated
	 * to the Product with id 1 and checks if the downloaded content is correct.
	 */
	@Test
	void testDownload() {
		String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor "
				+ "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation"
				+ " ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
				+ " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat"
				+ " cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		try {
			String body = 
				Jsoup.connect(BASE_HOST + "/dbadmin/download/tech.ailef.dbadmin.test.models.Product/image/1")
					 .execute().body();
			assertEquals(body.trim(), loremIpsum);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}








