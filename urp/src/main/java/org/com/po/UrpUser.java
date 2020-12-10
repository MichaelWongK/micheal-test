package org.com.po;
import java.util.Date;

import org.com.annotation.Bean;
@Bean
public class UrpUser{
	private String department;
	private Byte type;
	private String userName;
	private Integer userTell;
	private Integer userId;
	private Date createTime;
	private String password;
	public UrpUser(){
		
	}
	public UrpUser(String department, Byte type, String userName, Integer userTell, Integer userId, Date createTime,
			String password) {
		super();
		this.department = department;
		this.type = type;
		this.userName = userName;
		this.userTell = userTell;
		this.userId = userId;
		this.createTime = createTime;
		this.password = password;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartment() {
		return department;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getType() {
		return type;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserTell(Integer userTell) {
		this.userTell = userTell;
	}
	public Integer getUserTell() {
		return userTell;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public String toString() {
		return "UrpUser [department=" + department + ", type=" + type + ", userName=" + userName + ", userTell="
				+ userTell + ", userId=" + userId + ", createTime=" + createTime + ", password=" + password + "]";
	}
	
}