package tech.ailef.snapadmin.test.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import tech.ailef.snapadmin.external.annotations.Disable;

@Entity
@Disable
public class IgnoredEntity {
	@Id
	private String id;
	
	private int test;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}
	
}
