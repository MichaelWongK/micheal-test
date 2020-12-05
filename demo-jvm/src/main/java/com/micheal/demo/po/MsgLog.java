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

	public MsgLog() {
	}

	public MsgLog(String msg, Date nextTryTime, Date updateTime, Date createTime, Integer tryCount, String exchange, String msgId, String routingKey, Integer status) {
		this.msg = msg;
		this.nextTryTime = nextTryTime;
		this.updateTime = updateTime;
		this.createTime = createTime;
		this.tryCount = tryCount;
		this.exchange = exchange;
		this.msgId = msgId;
		this.routingKey = routingKey;
		this.status = status;
	}

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

	@Override
	public String toString() {
		return "MsgLog{" +
				"msg='" + msg + '\'' +
				", nextTryTime=" + nextTryTime +
				", updateTime=" + updateTime +
				", createTime=" + createTime +
				", tryCount=" + tryCount +
				", exchange='" + exchange + '\'' +
				", msgId='" + msgId + '\'' +
				", routingKey='" + routingKey + '\'' +
				", status=" + status +
				'}';
	}
}