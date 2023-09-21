package tech.ailef.dbadmin.test;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	private Random random = new Random();
	
//	@Bean(name = "internalDatasource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource internalDatasource() {
//        return DataSourceBuilder.create().url("jdbc:h2:file:./dbadmin").username("sa").password("password").build();
//    }
//	
//	@Bean
//	@ConfigurationProperties(prefix="spring.datasource")
//	public JdbcTemplate internalJdbc(@Qualifier("internalDatasource") DataSource dataSource) {
//	    return new JdbcTemplate(dataSource);
//	}
	
//	@Bean
//	public DbAdminInitializer dbAdmin() {
//		return new DbAdminInitializer("tech.ailef.caseclosed.webapp.models");
//	}
	
	private String[] USER_IDS = new String[] {
		"d7558967-c177-40f1-8360-25c7806329df", "3ccff81d-9f57-44b4-b414-5dc8bed05a28", 
		"c07ed80f-8658-40af-b5d1-bcda05f8e115", "ac0cf5a2-e5cf-49a7-855f-3bd2c0a79550", 
		"ffd5500e-1231-48e2-8384-3dc15fc7ed90", "471620c4-d859-49cd-b17b-5a27250d44a8", 
		"969775e5-95df-4b68-8e41-56bb781276c6", "98e2386a-5510-456c-88ea-60854a590b17", 
		"24e137ba-d5b5-4ca7-aad1-5359463e0a53", "6e21105f-3d24-4ca1-9fc0-b4e688992557"
	};
	
	private String randomUserId() {
		return USER_IDS[random.nextInt(USER_IDS.length)];
	}

	@SuppressWarnings("unused")
	private void populateDb() {
	   	int numOrders = 500;
	   	int startOrderLineId = 500;
	   	int startOrderId = 510;
    	for (int i = 0; i < numOrders; i++) {
    		String userId = randomUserId();
    		LocalDate randomDate = 
    			LocalDate.of(random.nextInt(2020, 2023), random.nextInt(12) + 1, 25).plusDays(random.nextInt(30));
    		System.out.println("INSERT INTO ORDERS (id, user_id, created_at) VALUES (" 
    				+ (i + startOrderId + 1) + ", '" + userId + "', '" + randomDate.toString() + "');");
    		
    		int numOrderLines = random.nextInt(1, 3);
    		for (int k = 0; k < numOrderLines; k++) {
    			System.out.println("INSERT INTO ORDER_LINE (id, order_id, price, quantity, product_id) "
    					+ "VALUES (" + startOrderLineId + ", " + (i + startOrderId + 1) + ", " + random.nextDouble(100, 900) 
    					+ ", " + random.nextInt(1, 3) + ", " + random.nextInt(50, 71) + ");");
    			startOrderLineId++;
    		}
    	}
	}
	
	
    @Override
    public void run(String...args) throws Exception {
//    	populateDb();
    }
}