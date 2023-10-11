package tech.ailef.dbadmin.test.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class ValidatedItem {
	@Id
	private String id;
	
	@ManyToOne
	@NotNull
	private Cart cart;
	
	@Size(min=5, max=10, message="Name must be 5-10 characters")
	@NotNull
	private String name;
	
	@Min(0)
	@NotNull
	private Integer number;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "ValidatedItem [id=" + id + ", cart=" + cart + ", name=" + name + ", number=" + number + "]";
	}
	
	
}
