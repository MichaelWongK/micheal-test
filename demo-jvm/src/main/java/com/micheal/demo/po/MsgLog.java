package com.micheal.demo.po; 

import java.util.Date;
public class MsgLog{
	private String msg;
	private Date nextTryTime;
	private Date updateTime;
	private Date createTime;
	private Integer tryCount;
	private String exchange;
	private String msgId;
	private String routingKey;
	private Integer status;
	private void setMsg(String msg) {
		this.msg = msg;
	}
	private String getMsg() {
		return msg;
	}
	private void setNextTryTime(Date nextTryTime) {
		this.nextTryTime = nextTryTime;
	}
	private Date getNextTryTime() {
		return nextTryTime;
	}
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
	private void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}
	private Integer getTryCount() {
		return tryCount;
	}
	private void setExchange(String exchange) {
		this.exchange = exchange;
	}
	private String getExchange() {
		return exchange;
	}
	private void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	private String getMsgId() {
		return msgId;
	}
	private void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	private String getRoutingKey() {
		return routingKey;
	}
	private void setStatus(Integer status) {
		this.status = status;
	}
	private Integer getStatus() {
		return status;
	}
}