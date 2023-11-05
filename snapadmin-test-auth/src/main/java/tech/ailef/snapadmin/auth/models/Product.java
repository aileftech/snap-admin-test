package tech.ailef.snapadmin.auth.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import tech.ailef.snapadmin.external.annotations.DisplayFormat;
import tech.ailef.snapadmin.external.annotations.DisplayImage;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;
import tech.ailef.snapadmin.external.annotations.FilterableType;
import tech.ailef.snapadmin.external.annotations.ReadOnly;

@Entity
@Table(name="products")
public class Product {
	@Id
	private Long id;
	
	@Filterable
	@Column(nullable = false)
	private String name;
	
	@DisplayFormat(format = "$%.2f")
	@Filterable
	@Column(nullable = false)
	private Double price;

	@Lob
	@DisplayImage
	@Column(length = 1048576*4)
	private byte[] image;
	
	@Filterable
	@Lob
	private String description;
		
	@Filterable
	@ReadOnly
	private LocalDateTime createdAt;
	
	@Lob
	@Column(length = 1048576*4)
	private byte[] thumbnailImage;
	
	@Column(columnDefinition = "boolean default false")
	@Filterable(type=FilterableType.CATEGORICAL)
	private Boolean ecoFriendly;
	
	@DisplayName
	public String getDisplayName() {
		return name + " $" + price;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public Boolean isEcoFriendly() {
		return ecoFriendly;
	}
	
	public void setEcoFriendly(boolean ecoFriendly) {
		this.ecoFriendly = ecoFriendly;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public byte[] getThumbnailImage() {
		return thumbnailImage;
	}
	
	public void setThumbnailImage(byte[] thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	
	public void setEcoFriendly(Boolean ecoFriendly) {
		this.ecoFriendly = ecoFriendly;
	}

}
