package tech.ailef.snapadmin.test.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import tech.ailef.snapadmin.external.annotations.Filterable;
import tech.ailef.snapadmin.external.annotations.FilterableType;

@Entity
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Cart cart;
	
	@ManyToOne
	@Filterable(type=FilterableType.CATEGORICAL)
	private Product product;
	
	@Filterable(type=FilterableType.CATEGORICAL)
	private Integer quantity;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
