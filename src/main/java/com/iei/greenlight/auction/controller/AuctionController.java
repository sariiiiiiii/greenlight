package com.iei.greenlight.auction.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.iei.greenlight.auction.domain.AdminPageInfo;
import com.iei.greenlight.auction.domain.Auction;
import com.iei.greenlight.auction.domain.AuctionHistory;
import com.iei.greenlight.auction.domain.AuctionImage;
import com.iei.greenlight.auction.domain.AuctionSuccessFul;
import com.iei.greenlight.auction.domain.AuctionUser;
import com.iei.greenlight.auction.domain.PageInfo;
import com.iei.greenlight.auction.service.AuctionService;
import com.iei.greenlight.common.AdminAuctionPagination;
import com.iei.greenlight.common.AuctionPagination;
import com.iei.greenlight.mypage.service.MyPageService;
import com.iei.greenlight.user.domain.User;
import com.iei.greenlight.user.service.UserService;

@Controller
public class AuctionController {
	
	@Autowired
	private AuctionService service;
	@Autowired
	private UserService userService;
	@Autowired
	private MyPageService mypageService;
	 
	// 관리자 재고 페이지 이동
	@RequestMapping(value="adminAuctionView.do")
	public String adminAuctionPageView(HttpServletRequest request, Model model, @RequestParam(value="page", required = false) Integer page) {
		
		int currentPage = (page != null) ? page : 1;
		int totalCount = service.getAdminListCount();
		AdminPageInfo pi = AdminAuctionPagination.getPageInfo(currentPage, totalCount);
		List<Auction> aList = service.printAuctionAllList(pi);
		if(!aList.isEmpty()) {
			model.addAttribute("aList", aList);
			model.addAttribute("pi", pi);
			return "admin/adminAuction";
		}else {
			model.addAttribute("pi", pi);
			model.addAttribute("aList", null);
			return "admin/adminAuction";
		}
	}
	
	// 관리자 판매 페이지 이동
	@RequestMapping(value="adminSellAuctionView.do")
	public String adminSellAuctionPageView(Model model, @RequestParam(value="page", required = false) Integer page) {
		
		int currentPage = (page != null) ? page : 1;
		int totalCount = service.getAdminListCount();
		AdminPageInfo pi = AdminAuctionPagination.getPageInfo(currentPage, totalCount);
		List<AuctionSuccessFul> sList = service.printSuccessFulList();
		if(!sList.isEmpty()) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			return "admin/adminSellAuction";
		}else {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", null);
			return "admin/adminSellAuction";
		}
	}
	
	// 리스트 페이지 이동
	@RequestMapping(value="auctionListView.do")
	public String auctionListView(HttpServletRequest request, Model model, @RequestParam(value="page", required = false) Integer page) {
			int currentPage = (page != null) ? page : 1;
			int totalCount = service.getListCount();
			PageInfo pi = AuctionPagination.getPageInfo(currentPage, totalCount);
			List<AuctionHistory> aList = service.printAllList(pi);
			
			if(!aList.isEmpty()) {
				model.addAttribute("pi", pi);
				model.addAttribute("aList", aList);
				return "auction/auctionList";
			}else {
				model.addAttribute("pi", pi);
				model.addAttribute("aList", null);
				return "auction/auctionList";
			}
	}
	
	// 관리자 경매 승인
	@RequestMapping(value="registAuctionHistory.do", method=RequestMethod.POST)
	public String registAuctionHistory(Model model, @RequestParam("auctionNo") int[] auctionNo) {
		
		List<Auction> aList = new ArrayList<Auction>();
		List<AuctionHistory> hList = new ArrayList<AuctionHistory>();
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd-HH-mm-ss");
		Date time = new Date();
		String time1 = format1.format(time);
		for(int i = 0; i < auctionNo.length; i++) {
			Auction auction = service.printAuctionOneByNo(auctionNo[i]);
			AuctionHistory auctionHistory = new AuctionHistory(auction.getAuctionNo(), auction.getUserId(), time1, auction.getAuctionTitle(), auction.getAuctionTime(), auction.getAuctionPrice());
			hList.add(auctionHistory);
		}
		
		int result = service.registerAuctionHistory(hList);
		if(result > 0) {
			int remove = service.removeAuction(auctionNo);
		}
		return "redirect:adminAuctionView.do";
	}
	
	// 경매 디테일 페이지
	@RequestMapping(value="auctionDetail.do")
	public String auctionDetailView(@RequestParam("auctionNo") int auctionNo, Model model, HttpServletRequest request) {
		
		try {
			AuctionUser auctionUser = service.printAuctionUser(auctionNo);
			User user = userService.printUser((String)request.getSession().getAttribute("userId"));
			AuctionHistory auctionHistory = service.printAuctionHistoryOneByNo(auctionNo);
			if(auctionHistory != null) {
				List<AuctionImage> imageList = service.printAuctionImageOneByNo(auctionNo);
				model.addAttribute("auctionUser", auctionUser);
				model.addAttribute("user", user);
				model.addAttribute("auctionHistory", auctionHistory);
				model.addAttribute("auctionImage", imageList);
				return "auction/auctionDetail";
			}else {
				model.addAttribute("msg", "디테일 조회 실패");
				return "common/errorPage";
			}
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.toString());
			return "common/errorPage";
		}
	}
	
	// 경매 신청글 페이지 이동
	@RequestMapping(value="auctionWrite.do")
	public String auctionWriteView() {
		
		return "auction/auctionWrite";
		
	}

	// 경매 신청
	@RequestMapping(value="auctionApply.do", method=RequestMethod.POST)
	public String auctionApply(@ModelAttribute Auction auction, HttpServletRequest request, Model model, @RequestParam("uploadFiles") MultipartFile[] uploadFiles) {
		
		auction.setUserId((String)request.getSession().getAttribute("userId")); // session값에서 아이디 가지고 오기
		List<AuctionImage> aList = null;
		
		try {
			SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd-HH-mm-ss");
			Date time = new Date();
			String time1 = format1.format(time);
			auction.setVarRegDate(time1);
			int result = service.registerAuction(auction); // insert 됐을 시 auctionNo 을 가져와야되
			if(result > 0) {
				aList = new ArrayList<AuctionImage>();
				for(MultipartFile uploadFile : uploadFiles) {
					if(!uploadFile.getOriginalFilename().equals("")) {
						AuctionImage auctionImage = saveFile(uploadFile, request);
						auctionImage.setAuctionNo(auction.getAuctionNo()); // selectKey를 이용한 primaryKey 값 가져오기
						auctionImage.setUserId((String)request.getSession().getAttribute("userId")); // 임의 아이디
						auctionImage.setFileSize(uploadFile.getSize());
						auctionImage.setFileMain("N");
						aList.add(auctionImage);
					}
				}
				aList.get(0).setFileMain("Y"); // 첫번째 사진 mainImage 등록
				int image = service.registerAuctionImage(aList);
				return "redirect:auctionListView.do";
			}else {
				model.addAttribute("msg", "경매 신청글 실패");
				return "common/errorPage";
			}
		}catch(Exception e) {
			model.addAttribute("msg", e.toString());
			return "common/errorPage";
		}
	}
	
	// 경매 신청 파일 저장
	public AuctionImage saveFile(MultipartFile uploadFile, HttpServletRequest request) {
		
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "\\auctionImage"; // 경로 생성
		File folder= new File(savePath);
		if(!folder.exists()) {
			folder.mkdir(); // 폴더생성
		}
		
		String filePath = folder + "\\" + uploadFile.getOriginalFilename(); // 파일 절대 경로
		String fileName = uploadFile.getOriginalFilename();
		
		try {
			uploadFile.transferTo(new File(filePath)); // 파일 업로드
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AuctionImage auctionImage = new AuctionImage(fileName, filePath);
		return auctionImage;
	}
	

	
	@RequestMapping(value="insertAuctionUser.do")
	public String insertAuctionUser(@ModelAttribute AuctionUser auctionUser) {
		int result = service.registerAuctionUser(auctionUser);
		
		return "redirect:auctionDetail.do?auctionNo="+auctionUser.getAuctionNo();
	}
	
	
	// 경매 마감시 낙찰페이지 이동(중요)
	@RequestMapping(value="insertAuctionSuccessFul.do")
	public String insertAuctionSuccessFul(@RequestParam("auctionNo") int auctionNo) {
		
		int result = service.modifyAuctionHistory(auctionNo); // 끝난경매 상태 수정
		AuctionHistory auctionHistory = service.printAuctionHistoryOneByNo(auctionNo); // 끝난 경매 정보 조회
		AuctionUser auctionUser = service.printAuctionUser(auctionNo); // 최고 입찰자 조회
		if(auctionUser != null) {
			AuctionSuccessFul auctionSuccessFul = new AuctionSuccessFul(auctionNo, auctionUser.getUserId(), auctionHistory.getUserId(), auctionUser.getPoint(), auctionHistory.getAuctionTitle());
			int auctionResult = service.registerAuctionSuccessFul(auctionSuccessFul); // 경매 낙찰자 페이지 이동
		}else {
			AuctionSuccessFul auctionSuccessFul = new AuctionSuccessFul(auctionNo, auctionHistory.getUserId(), auctionHistory.getUserId(), 0, auctionHistory.getAuctionTitle());
			int auctionResult = service.registerAuctionSuccessFul(auctionSuccessFul); // 경매 낙찰자 페이지 이동
		}
		
		return "redirect:auctionListView.do";
	}
	
	// 관리자 포인트지급 및 회수
	@RequestMapping(value="userPayPoint.do", method=RequestMethod.POST)
	public String userPayPoint(Model model, @RequestParam("auctionNo") int[] auctionNo, HttpServletRequest request) {
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		HashMap<String, Object> pointMap = new HashMap<String, Object>();
		HashMap<String, Object> chargePointMap = new HashMap<String, Object>();
		map.put("auctionNo", auctionNo);
		
		List<AuctionSuccessFul> sList = service.printSuccessFulByNo(map);
		for(AuctionSuccessFul auctionSuccessFul : sList) {
			User user = userService.printUser(auctionSuccessFul.getBuyer());
			int auctionPrice = auctionSuccessFul.getAuctionPrice();
			String buyer = auctionSuccessFul.getBuyer();
			if(auctionSuccessFul.getAuctionPrice() == 0) {
				
			}else if(user.getPoint() < auctionPrice) { // 낙찰가격이 적립포인트보다 적으면
				int point = auctionPrice - user.getPoint(); // 낙찰가격에서 적립포인트를 뺀 금액
				int buyPoint = user.getPoint(); // 적립포인트 금액
				if(user.getPoint() != 0) { // 적립포인트가 0원이 아니면
					pointMap.put("userId", buyer);
					pointMap.put("point", 0);
					mypageService.registerAuctionBuyerPoint(user); // 적립포인트 pointhistory
					userService.modifyUserPoint(pointMap); // 적립포인트를 0으로 update
					chargePointMap.put("userId", buyer);
					chargePointMap.put("point", point);
					mypageService.registerAuctionBuyerChargePoint(chargePointMap);
					userService.modifyUserChargePoint(chargePointMap); // 낙찰가격 - 적립포인트를 뺀 금액을 충전포인트에서 차감
				}else {
					chargePointMap.put("userId", buyer);
					chargePointMap.put("point", point);
					mypageService.registerAuctionBuyerChargePoint(chargePointMap);
					userService.modifyUserChargePoint(chargePointMap); // 적립포인트가 0원이면 바로 충전포인트에서 차감
				}
			}else { // 적립포인트가 낙찰포인트보다 많으면
				pointMap.put("userId", buyer);
				pointMap.put("point", auctionPrice);
				mypageService.registerAuctionBuyerPointHistory(pointMap);
				userService.modifyUserMinusPoint(pointMap); // 적립포인트에서 낙찰가격 차감
			}
			pointMap.put("userId", auctionSuccessFul.getSeller());
			pointMap.put("point", auctionPrice);
			mypageService.registerAuctionSellerPointHistory(pointMap);
			userService.modifySellerPoint(pointMap); // 판매자에게 금액 추가
		}
		
		service.modifyAuctionSuccessFul(auctionNo); // 낙찰자 상태 수정
		service.removeAuctionUser(auctionNo); // 입찰자 삭제
		for(int i = 0; i < auctionNo.length; i++) { // 이미지 삭제
			List<AuctionImage> aList = service.printAuctionImageOneByNo(auctionNo[i]);
			for(AuctionImage a : aList) {
				deleteFile(a.getFileName(), request);
			}
		}
		service.removeAuctionImage(auctionNo); // 낙찰 이미지 삭제
		
		return "redirect:adminSellAuctionView.do";
	}
	
	public void deleteFile(String fileName, HttpServletRequest request) {
		
		String root = request.getSession().getServletContext().getRealPath("resources");
		String fullPath = root + "\\auctionImage";
		File file = new File(fullPath + "\\" + fileName);
		if(file.exists()) {
			file.delete();
		}
	}
	
	
	// 내가 올린 경매
	@RequestMapping(value = "myAcution.do", method = RequestMethod.GET)
	public String myAuction(Model model, HttpSession session, @RequestParam(value="page", required = false) Integer page) {
		try {
			String userId = (String) session.getAttribute("userId");
			int currentPage = (page != null) ? page : 1;
			int totalCount = service.getMyListCount(userId);
			PageInfo pi = AuctionPagination.getPageInfo(currentPage, totalCount);
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("pi", pi);
			hashMap.put("userId", userId);
			List<AuctionHistory> aList = service.printAllList(hashMap);
			if (!aList.isEmpty()) {
				model.addAttribute("aList", aList);
				model.addAttribute("pi", pi);
				return "mypage/MyAuction";
			} else {
				model.addAttribute("aList", null);
				model.addAttribute("pi", pi);
				// 11/11 수정
				return "mypage/MyAuction";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.toString());
			return "common/errorPage";
		}
	}

	// 내가 입찰한 경매
	@RequestMapping(value = "myBidList.do", method = RequestMethod.GET)
	public String myBidList(Model model, HttpSession session, @RequestParam(value="page", required = false) Integer page) {

		String userId = (String) session.getAttribute("userId");
		int currentPage = (page != null) ? page : 1;
		int totalCount = service.getMyAuctionCount(userId);
		PageInfo pi = AuctionPagination.getPageInfo(currentPage, totalCount);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("pi", pi);
		hashMap.put("userId", userId);
		List<AuctionHistory> aList = service.printList(hashMap);
		if (aList != null) {
			model.addAttribute("aList", aList);
			model.addAttribute("pi", pi);
			return "mypage/MyBidList";
		} else {
			model.addAttribute("aList", null);
			model.addAttribute("pi", pi);
			return "mypage/MyBidList";
		}
	}

}
