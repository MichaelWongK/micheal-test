package com.micheal.demo.po; 

public class User{
	private String password;
	private Integer id;
	private String username;
	private void setPassword(String password) {
		this.password = password;
	}
	private String getPassword() {
		return password;
	}
	private void setId(Integer id) {
		this.id = id;
	}
	private Integer getId() {
		return id;
	}
	private void setUsername(String username) {
		this.username = username;
	}
	private String getUsername() {
		return username;
	}
}