package com.micheal.demo.po; 

import java.util.Date;
public class LoginLog{
	private Date updateTime;
	private Date createTime;
	private Integer userId;
	private String description;
	private Integer id;
	private Integer type;
	private String msgId;
	private void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	private Date getUpdateTime() {
		return updateTime;
	}
	private void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private Date getCreateTime() {
		return createTime;
	}
	private void setUserId(Integer userId) {
		this.userId = userId;
	}
	private Integer getUserId() {
		return userId;
	}
	private void setDescription(String description) {
		this.description = description;
	}
	private String getDescription() {
		return description;
	}
	private void setId(Integer id) {
		this.id = id;
	}
	private Integer getId() {
		return id;
	}
	private void setType(Integer type) {
		this.type = type;
	}
	private Integer getType() {
		return type;
	}
	private void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	private String getMsgId() {
		return msgId;
	}
}