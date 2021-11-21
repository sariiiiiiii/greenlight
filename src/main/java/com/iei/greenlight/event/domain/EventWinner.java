package com.iei.greenlight.event.domain;

import java.sql.Date;

public class EventWinner {
	
	private int eventNo;
	private String userId;
	private Date joinDate;
	
	private EventWinner() {}

	public EventWinner(int eventNo, String userId, Date joinDate) {
		super();
		this.eventNo = eventNo;
		this.userId = userId;
		this.joinDate = joinDate;
	}

	public int getEventNo() {
		return eventNo;
	}

	public void setEventNo(int eventNo) {
		this.eventNo = eventNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	@Override
	public String toString() {
		return "EventWinner [eventNo=" + eventNo + ", userId=" + userId + ", joinDate=" + joinDate + "]";
	}
	
	

}
