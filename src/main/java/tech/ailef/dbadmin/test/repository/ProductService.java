package tech.ailef.dbadmin.test.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.ailef.dbadmin.test.models.Product;

@Service
public class ProductService {
	@Autowired
	private ProductRepository repo;
	
	@Transactional
	public Product upsert(Product p) {
		return repo.save(p);
	}
}
