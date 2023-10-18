package tech.ailef.dbadmin.test.models;

import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.ComputedColumn;
import tech.ailef.dbadmin.external.annotations.DisableExport;
import tech.ailef.dbadmin.external.annotations.DisplayName;
import tech.ailef.dbadmin.external.annotations.HiddenColumn;

@Entity
@Table(name="users")
@DisableExport
public class User {
	@Id
	@UuidGenerator
	@GeneratedValue
	private String id;
	
	private String name;

	@HiddenColumn
	@Column(nullable = false)
	private String password;
	
	@OneToOne
	private Cart cart;
	
	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DisplayName
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public List<Order> getOrders() {
		return orders;
	}
	
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@ComputedColumn
	public double numberOfOrders() {
		return orders.size();
	}
	
}
