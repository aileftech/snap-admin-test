package tech.ailef.dbadmin.test.models;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tech.ailef.dbadmin.external.annotations.DisplayFormat;
import tech.ailef.dbadmin.external.annotations.DisplayImage;
import tech.ailef.dbadmin.external.annotations.DisplayName;
import tech.ailef.dbadmin.external.annotations.Filterable;
import tech.ailef.dbadmin.external.annotations.FilterableType;

@Entity
@Table(name="products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Filterable
	@Column(nullable = false)
	private String name;
	
	@Filterable
	@Lob
	private String description;
	
	@DisplayFormat(format = "$%.2f")
	@Filterable
	@Column(nullable = false)
	private Double price;
	
	@Filterable
	private LocalDateTime createdAt;
	
	@Lob
	@DisplayImage
	@Column(length = 1048576*4)
	private byte[] image;
	
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
	
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", image="
				+ Arrays.toString(thumbnailImage) + ", categories=" + categories + ", orderLines=" + orderLines + "]";
	}
	
}
