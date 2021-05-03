package com.foa.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Staff {
	@SerializedName("id")
	private String id;
	@SerializedName("username")
	private String userName;
	@SerializedName("password")
	private String password;
	@SerializedName("fullName")
	private String fullName;
	@SerializedName("firstName")
	private String firstName;
	@SerializedName("lastName")
	private String lastName;
	@SerializedName("phone")
	private String phone;
	@SerializedName("IDNumber")
	private String IDNumber;
	@SerializedName("restaurantId")
	private String restaurantId;

	public Staff(String id, String userName, String password, String fullName, String firstName, String lastName, String phone, String IDNumber, String restaurantId) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.IDNumber = IDNumber;
		this.restaurantId = restaurantId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String IDNumber) {
		this.IDNumber = IDNumber;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

}
