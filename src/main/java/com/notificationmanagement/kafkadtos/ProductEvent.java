package com.notificationmanagement.kafkadtos;

import java.time.LocalDateTime;

public class ProductEvent {
	private Long productId;
	private String title;
	private String description;
	private String author;
	private String isbn;
	private double price;
	private String categoryName;
	private LocalDateTime createdAt;

	public ProductEvent() {
	}

	public ProductEvent(Long productId, String title, String description, String author, String isbn, double price,
			String categoryName, LocalDateTime createdAt) {
		this.productId = productId;
		this.title = title;
		this.description = description;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.categoryName = categoryName;
		this.createdAt = createdAt;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
