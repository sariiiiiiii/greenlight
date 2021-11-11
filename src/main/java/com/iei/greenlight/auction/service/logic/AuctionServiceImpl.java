package com.iei.greenlight.auction.service.logic;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iei.greenlight.auction.domain.AdminPageInfo;
import com.iei.greenlight.auction.domain.Auction;
import com.iei.greenlight.auction.domain.AuctionHistory;
import com.iei.greenlight.auction.domain.AuctionImage;
import com.iei.greenlight.auction.domain.AuctionSuccessFul;
import com.iei.greenlight.auction.domain.AuctionUser;
import com.iei.greenlight.auction.domain.PageInfo;
import com.iei.greenlight.auction.service.AuctionService;
import com.iei.greenlight.auction.store.AuctionStore;

@Service
public class AuctionServiceImpl implements AuctionService{
	
	@Autowired
	private AuctionStore store;
	
	@Override
	public List<Auction> printAuctionAllList(AdminPageInfo pi) {
		
		List<Auction> aList = store.selectAuctionAllList(pi);
		
		return aList;
	}
	

	@Override
	public List<AuctionHistory> printAllList(PageInfo pi) {
		
		List<AuctionHistory> aList = store.selectAllList(pi);
		
		return aList;
	}


	@Override
	public int getAdminListCount() {
		
		int totalCount = store.selectAdminListCount();
		
		return totalCount;
	}
	
	@Override
	public int getListCount() {
		
		int totalCount = store.selectListCount();
		
		return totalCount;
	}


	@Override
	public Auction printAuctionOneByNo(int auctionNo) {
		
		Auction auction = store.selectAuctionOneByNo(auctionNo);
		
		return auction;
	}


	@Override
	public AuctionHistory printAuctionHistoryOneByNo(int auctionNo) {
		
		AuctionHistory auctionHistory = store.selectAuctionHistoryOneByNo(auctionNo);
		
		return auctionHistory;
	}

	@Override
	public List<AuctionImage> printAuctionImageOneByNo(int auctionNo) {
		
		List<AuctionImage> imageList = store.selectAuctionImageOneByNo(auctionNo);
		
		return imageList;
	}
	

	@Override
	public int registerAuction(Auction auction) {
		
		int result = store.insertAuction(auction);
		
		return result;
	}
	

	@Override
	public int removeAuction(int[] auctionNo) {
		
		int result = store.deleteAuction(auctionNo);
		
		return result;
	}


	@Override
	public int registerAuctionImage(List<AuctionImage> aList) {
		
		int result = store.insertAuctionImage(aList);
		
		return result;
	}
	
	
	@Override
	public int registerAuctionHistory(List<AuctionHistory> hList) {
		
		int result = store.insertAuctionHistory(hList);
		
		return result;
	}
	
	

	@Override
	public int modifyAuctionHistory(int auctionNo) {
		
		int result = store.updateAuctionHistory(auctionNo);
		
		return result;
	}



	@Override
	public AuctionUser printAuctionUser(int auctionNo) {
		
		AuctionUser auctionUser = store.selectAuctionUser(auctionNo);
		
		return auctionUser;
	}


	@Override
	public int registerAuctionUser(AuctionUser auctionUser) {
		
		int result = store.insertAuctionUser(auctionUser);
		
		return result;
	}


	@Override
	public List<AuctionSuccessFul> printSuccessFulList() {
		
		List<AuctionSuccessFul> sList = store.selectAuctionSuccessFul();
		
		return sList;
	}


	@Override
	public List<AuctionSuccessFul> printSuccessFulByNo(HashMap<String, int[]> map) {

		List<AuctionSuccessFul> sList = store.selectAuctionSuccessFulByNo(map);
		
		return sList;
	}
	
	@Override
	public int registerAuctionSuccessFul(AuctionSuccessFul auctionSuccessFul) {
		
		int result = store.insertAuctionSuccessFul(auctionSuccessFul);
		
		return result;
	}

	@Override
	public int modifyAuctionSuccessFul(int[] auctionNo) {
		
		int result = store.updateAuctionSuccessFul(auctionNo);
		
		return result;
	}
	

	// 서비스 로직
	@Override
	public List<AuctionHistory> printAllList(String userId) {
		System.out.println("서비스 : " + userId);
		List<AuctionHistory> aList = store.selectAllList(userId);
		return aList;
	}

	@Override
	public List<AuctionHistory> printList(String userId) {
		List<AuctionHistory> aList = store.selectList(userId);
		return aList;
	}


}
