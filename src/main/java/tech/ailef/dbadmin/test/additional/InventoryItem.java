package tech.ailef.dbadmin.test.additional;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.DisableCreate;

@Entity
@Table(name="inventory")
@DisableCreate
public class InventoryItem {
	@Id
	private String id;
	
	private String name;
	
	private Boolean available;
	
	private Calendar createdAt;

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

	public Boolean isAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
