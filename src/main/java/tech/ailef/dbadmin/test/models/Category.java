package tech.ailef.dbadmin.test.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import tech.ailef.snapadmin.external.annotations.DisableExport;
import tech.ailef.snapadmin.external.annotations.DisplayName;

@Entity
@DisableExport
public class Category {
	@Id
	private Long id;
	
	private String name;
	
	@ManyToMany
	private List<Product> products;
	
	@ManyToMany
	private List<Product> starredProducts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@DisplayName
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public List<Product> getStarredProducts() {
		return starredProducts;
	}
	
	public void setStarredProducts(List<Product> starredProducts) {
		this.starredProducts = starredProducts;
	}
}
