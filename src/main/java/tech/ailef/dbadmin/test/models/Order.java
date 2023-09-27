package tech.ailef.dbadmin.test.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.ComputedColumn;
import tech.ailef.dbadmin.external.annotations.Filterable;
import tech.ailef.dbadmin.external.annotations.FilterableType;

@Entity
@Table(name="orders")
public class Order {
	@Id
	private Long id;
	
	@ManyToOne
	@Filterable(type=FilterableType.CATEGORICAL)
	private User user;
	
	@OneToMany(mappedBy = "order")
	private List<OrderLine> lines;
	
	@Filterable
	private LocalDate createdAt;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderLine> getLines() {
		return lines;
	}

	public void setLines(List<OrderLine> lines) {
		this.lines = lines;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
	@ComputedColumn(name="total_value")
	public double total() {
		double total = 0;
		for (OrderLine l : lines) {
			total += l.getQuantity() * l.getPrice().doubleValue();
		}
		return total;
	}
	
	
}
