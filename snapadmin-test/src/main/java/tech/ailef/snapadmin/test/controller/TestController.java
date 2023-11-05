package tech.ailef.snapadmin.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tech.ailef.snapadmin.test.models.Product;
import tech.ailef.snapadmin.test.repository.ProductService;

@Controller
@RequestMapping("/")
public class TestController {
	@Autowired
	private ProductService service;
	
	@GetMapping
	public String index() {
		Product p = new Product();
		p.setId(999L);
		p.setName("Test");
		service.upsert(p);
		return "TEST";
	}
}
