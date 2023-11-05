package tech.ailef.snapadmin.test.models;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
	
	@ManyToMany(mappedBy = "products")
	@Filterable
	private List<Category> categories;
	
	@ManyToMany(mappedBy = "starredProducts")
	private List<Category> starredIn;
	
	@ManyToMany
	private List<Tag> tags;
	
	@OneToMany(mappedBy = "product")
	private List<OrderLine> orderLines;
	
	@Enumerated(EnumType.STRING)
	@Filterable(type=FilterableType.CATEGORICAL)
	private ProductStatus status;

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
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public List<OrderLine> getOrderLines() {
		return orderLines;
	}
	
	public void setLines(List<OrderLine> lines) {
		this.orderLines = lines;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public List<Category> getStarredIn() {
		return starredIn;
	}
	
	public void setStarredIn(List<Category> starredIn) {
		this.starredIn = starredIn;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
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

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", image="
				+ Arrays.toString(thumbnailImage) + ", categories=" + categories + ", orderLines=" + orderLines + "]";
	}
	
}
