package tech.ailef.snapadmin.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
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
class SnapAdminAuthTestProjectApplicationTests {
	
	private static final String BASE_PACKAGE = "tech.ailef.snapadmin.test.models";

	private static final Logger logger = Logger.getLogger(SnapAdminAuthTestProjectApplicationTests.class.getName());
	
	private static final String DBADMIN_ROOT_PATH = "/admin";
	
	private static final String BASE_URL = "http://localhost:8080" + DBADMIN_ROOT_PATH;
	
//	
//	@Test
//	void testDisabledEntity() {
//		ChromeDriver driver = new ChromeDriver();
//		
//		driver.get(BASE_URL);
//		WebElement body = driver.findElement(By.cssSelector("body"));
//		assertEquals(false, body.getText().contains("ignored_entity"));
//		
//		driver.close();
//	}
	
}








