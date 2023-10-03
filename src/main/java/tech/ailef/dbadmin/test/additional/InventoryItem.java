package tech.ailef.dbadmin.test.additional;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.ReadOnly;

@Entity
@Table(name="inventory")
public class InventoryItem {
	@Id
	private String id;
	
	private String name;
	
	private Boolean available;

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

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
}
