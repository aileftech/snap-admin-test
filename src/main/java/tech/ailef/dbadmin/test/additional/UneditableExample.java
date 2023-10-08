package tech.ailef.dbadmin.test.additional;

import java.math.BigInteger;
import java.sql.Date;

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
	
	private short quantity;

	private BigInteger totalSales;
	
	private Date createdAt;
	
	private char category;
	
	private byte testByte;
	
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

	public short getQuantity() {
		return quantity;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	public BigInteger getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(BigInteger totalSales) {
		this.totalSales = totalSales;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public char getCategory() {
		return category;
	}

	public void setCategory(char category) {
		this.category = category;
	}

	public byte getTestByte() {
		return testByte;
	}

	public void setTestByte(byte testByte) {
		this.testByte = testByte;
	}
	
}
