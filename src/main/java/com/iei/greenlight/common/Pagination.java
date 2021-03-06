package com.iei.greenlight.common;

import com.iei.greenlight.challenge.domain.PageInfo;

public class Pagination {
	
	public static PageInfo getPageInfo(int currentPage, int totalCount) {
		
		PageInfo pi = null;
		
		int naviLimit = 5;  // 한 페이지에 보이는 네비갯수
		int boardLimit = 6; // 한 페이지에서 보이는 글 갯수
		int maxPage;        // 전체 페이지에서 제일 마지막 페이지
		int startNavi;      // 시작페이지 번호
		int endNavi;        // 끝 페이지 번호
		
		maxPage = (int)((double)totalCount/boardLimit + 0.9);
		startNavi = ((int)((double)currentPage/naviLimit + 0.9) - 1) * naviLimit + 1;
		endNavi = startNavi + naviLimit - 1;
		if(maxPage < endNavi) {
			endNavi = maxPage;
		}
		pi = new PageInfo(currentPage, boardLimit, naviLimit, startNavi, endNavi, totalCount, maxPage);
		return pi;
	}
}
