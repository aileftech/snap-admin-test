package tech.ailef.dbadmin.test.additional;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.DisableEdit;

@Entity
@Table(name="uneditable")
@DisableEdit
public class UneditableExample {
	@Id
	private String id;
	
	private String title;
	
	private int quantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
