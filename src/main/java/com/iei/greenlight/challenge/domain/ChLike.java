package com.iei.greenlight.challenge.domain;

public class ChLike {
	
	private int likeNo;
	private int chNo;
	private String userId;
	private String likeCk;
	
	public ChLike() {}
	
	public ChLike(int likeNo, int chNo, String userId, String likeCk) {
		super();
		this.likeNo = likeNo;
		this.chNo = chNo;
		this.userId = userId;
		this.likeCk = likeCk;
	}

	public int getLikeNo() {
		return likeNo;
	}

	public void setLikeNo(int likeNo) {
		this.likeNo = likeNo;
	}

	public int getChNo() {
		return chNo;
	}

	public void setChNo(int chNo) {
		this.chNo = chNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLikeCk() {
		return likeCk;
	}

	public void setLikeCk(String likeCk) {
		this.likeCk = likeCk;
	}

	@Override
	public String toString() {
		return "ChLike [likeNo=" + likeNo + ", chNo=" + chNo + ", userId=" + userId + ", likeCk=" + likeCk + "]";
	}
	
	
}
