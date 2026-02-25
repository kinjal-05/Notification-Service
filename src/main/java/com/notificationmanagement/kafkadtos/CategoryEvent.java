package com.notificationmanagement.kafkadtos;

public class CategoryEvent {
	private Long requestId;
	private String categoryName;
	private String description;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryEvent(Long requestId, String categoryName, String description) {
		this.requestId = requestId;
		this.categoryName = categoryName;
		this.description = description;
	}

	public CategoryEvent() {
	}
}
