package com.example.mongodb.objects;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User{
	@Id
	private long userId;
	private LocalDateTime createdAt;
	private String email;
	private String handle;
	private String password;
	private String confirmPassword;
	private String location;
	private String bio;
	private String website;
	private String imageUrl;
	public static final String SEQUENCE_NAME = "user_sequence";

	public User(String handle, String password,String confirmPassword,String email) {
		this.createdAt = LocalDateTime.now();
		this.handle = handle;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.email= email;
		this.imageUrl = "http://127.0.0.1:8877/no-image.png";
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long l) {
		this.userId = l;
	}



	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBio() {
		return this.bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	

	

	

	
	
}