package com.notificationmanagement.dtos;

import java.time.LocalDateTime;

public class ApiResponse {

	private LocalDateTime timestamp;
	private int status;
	private String message;

	public ApiResponse(int status, String message) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
	}

	// Getters
	public LocalDateTime getTimestamp() { return timestamp; }
	public int getStatus() { return status; }
	public String getMessage() { return message; }
}
