package tech.ailef.dbadmin.test.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import tech.ailef.snapadmin.external.annotations.DisableDelete;

@Entity
@DisableDelete
public class OrderLine {
	@Id
	private Long id;
	
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private Product product;
	
	private BigDecimal price;
	
	private Integer quantity;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
