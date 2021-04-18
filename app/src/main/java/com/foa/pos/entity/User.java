package com.foa.pos.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
	private String id;
	@SerializedName("phoneNumber")
	private String userName;
	@SerializedName("password")
	private String password;
	private Date lastLogin;
	private String level;
	private String cashierID;

	public User() {
	}

	public User(String id, String userName, String password, Date lastLogin, String level, String cashierID) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.lastLogin = lastLogin;
		this.level = level;
		this.cashierID = cashierID;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getCashierID() {
		return cashierID;
	}
	public void setCashierID(String cashierID) {
		this.cashierID = cashierID;
	}

	public static User getSampleUser(){
		return new User("1","Ng Van Xe","123",new Date(),"1","1");
	}
}
