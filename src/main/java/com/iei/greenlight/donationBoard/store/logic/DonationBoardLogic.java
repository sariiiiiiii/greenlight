package com.iei.greenlight.donationBoard.store.logic;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iei.greenlight.donationBoard.domain.Donation;
import com.iei.greenlight.donationBoard.domain.DonationBoard;
import com.iei.greenlight.donationBoard.domain.DonationReply;
import com.iei.greenlight.donationBoard.domain.DtFile;
import com.iei.greenlight.donationBoard.domain.PageInfo;
import com.iei.greenlight.donationBoard.store.DonationBoardStore;
import com.iei.greenlight.user.domain.User;

@Repository
public class DonationBoardLogic implements DonationBoardStore{

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int insertDonationBoard(DonationBoard db) {
		return sqlSession.insert("donationBoardMapper.insertDonationBoard", db);
	}

	@Override
	public int insertDtFile(List<DtFile> dFile) {
		return sqlSession.insert("donationBoardMapper.insertDtFile", dFile);
	}

	@Override
	public int getListCount() {
		return sqlSession.selectOne("donationBoardMapper.selectListCount");
	}

	@Override
	public List<DonationBoard> selectDonationBoardAllList(PageInfo pi) {
		int offset = (pi.getCurrentPage()-1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return sqlSession.selectList("donationBoardMapper.selectDonationBoardAllList", pi, rowBounds);
	}

	@Override
	public DonationBoard selectDonationBoardOne(int boardNo) {
		return sqlSession.selectOne("donationBoardMapper.selectDonationOne", boardNo);
	}

	@Override
	public List<DtFile> selectAllDonationBoardImageOneByNo(int boardNo) {
		return sqlSession.selectList("donationBoardMapper.selectDonationBoardImageList",boardNo);
	}

	@Override
	public int donationUserPoint(HashMap<String, Object> map) {
		String userId = (String)map.get("userId");
		int dPoint = Integer.parseInt(String.valueOf(map.get("donationPoint")));
		// ?????????
		int point = sqlSession.selectOne("donationBoardMapper.selectUserPoint", userId);
		int result = sqlSession.update("donationBoardMapper.donationUserPoint", map);
		// point?????? ??? ?????? ??????
		int userPoint = sqlSession.selectOne("donationBoardMapper.selectUserPoint", userId);
		if(userPoint < 0) {
			String pointContents = "??????";
			HashMap<String, Object> hMap = new HashMap<String, Object>();
			hMap.put("point", point);
			hMap.put("userId", userId);
			hMap.put("pointContents", pointContents);
			if(point != 0 && point - dPoint< 0) {
				// ???????????? ?????? ?????????
				sqlSession.insert("donationBoardMapper.insertNormalPointHistory", hMap);
			}
			int cPoint = -(userPoint);
			hMap.put("cPoint", cPoint);
			// ???????????? ??????????????? ????????????
			sqlSession.insert("donationBoardMapper.insertUseChargePointHistory", hMap);
			// point??? ?????????????????? ??????????????? 2??? ?????? ?????? point??? chargePoint??? ?????? ??????.
			// ???, point??? ?????? ???????????? ??????.
			sqlSession.update("donationBoardMapper.updateUserPointZero", userId);
			HashMap<String, Object> cMap = new HashMap<String, Object>(); // ?????? ????????? ???????????? ?????? ???
			cMap.put("donationPoint", userPoint);
			cMap.put("userId", userId);
			sqlSession.update("donationBoardMapper.updateUserChargePoint", cMap);
		}else {
			String pointContents = "??????(?????? ?????????)";
			map.put("pointContents", pointContents);
			sqlSession.insert("donationBoardMapper.insertdonationPointHistory", map);
		}
		return result;
	}

	@Override
	public void insertDonationUser(Donation donation) {
		sqlSession.insert("donationBoardMapper.insertDonationUser", donation);
	}

	@Override
	public void updateDonationBoardDonationAmount(DonationBoard db) {
		sqlSession.update("donationBoardMapper.updateDonationBoardDonationAmount", db);
	}

	@Override
	public List<Donation> selectDonationUserRanking(int boardNo) {
		return sqlSession.selectList("donationBoardMapper.selectDonationUserRanking", boardNo);
	}

	@Override
	   public List<DonationBoard> mySelectList(HashMap<String, Object> hashMap) {
	      PageInfo pi = (PageInfo)(hashMap.get("pi"));
	      int offset = (pi.getCurrentPage()-1) * pi.getBoardLimit();
	      RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
	      return sqlSession.selectList("donationBoardMapper.selectMyDonation", hashMap, rowBounds);
	   }

	@Override
	public int insertDonationReply(DonationReply donationReply) {
		return sqlSession.insert("donationBoardMapper.insertDonationReply", donationReply);
	}

	@Override
	public void updateBoardReplyCound(int boardNo) {
		sqlSession.update("donationBoardMapper.addBoardReplyCount", boardNo);
	}

	@Override
	public List<DonationReply> seelectAllReply(int boardNo) {
		return sqlSession.selectList("donationBoardMapper.selectAllReply", boardNo);
	}

	@Override
	public int updateBoardReplyContents(DonationReply donationReply) {
		return sqlSession.update("donationBoardMapper.updateBoardReplyContents", donationReply);
	}

	@Override
	public int deleteDonationReply(DonationReply donationReply) {
		return sqlSession.delete("donationBoardMapper.deleteBoardReply", donationReply);
	}

	@Override
	public int selectSearchDonationListCount(String searchKey) {
		return sqlSession.selectOne("donationBoardMapper.selectSearchBoardListCount", searchKey);
	}

	@Override
	public List<DonationBoard> selectDonationBoardSearchList(HashMap<String, Object> hashMap) {
		PageInfo pi = (PageInfo)hashMap.get("pi");
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset,pi.getBoardLimit());
		return sqlSession.selectList("donationBoardMapper.selectDonationBoardSearchList",hashMap.get("searchKey"), rowBounds);
	}

	@Override
	public int selectAdminDonationCount() {
		return sqlSession.selectOne("donationBoardMapper.selectAdminBoardListCount");
	}

	@Override
	public List<DonationBoard> selectAllAdminBoard(PageInfo pi) {
		int offset = (pi.getCurrentPage()-1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return sqlSession.selectList("donationBoardMapper.selectAdminBoardList",pi, rowBounds);
	}

	@Override
	public int selectAdminDonationSearchCount(String searchKey) {
		return sqlSession.selectOne("donationBoardMapper.selectAdminBoardSearchCount", searchKey);
	}

	@Override
	public List<DonationBoard> selectAdminDonationSearchList(HashMap<String, Object> hashMap) {
		PageInfo pi = (PageInfo)hashMap.get("pi");
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return sqlSession.selectList("donationBoardMapper.selectAdminBoardSearchList", hashMap.get("searchKey"), rowBounds);
	}

	@Override
	public void updateBoardReplyDeleteCount(int boardNo) {
		sqlSession.update("donationBoardMapper.updateBoardReplyDeleteCount", boardNo);
	}

	@Override
	public void updateDonationEnd(int boardNo) {
		sqlSession.update("donationBoardMapper.updateDonationEnd",boardNo);
	}

	@Override
	public List<DonationBoard> selectSuccessN() {
		return sqlSession.selectList("donationBoardMapper.selectSuccessN");
	}

	@Override
	public List<DtFile> printModifyViewFile(int boardNo) {
		return sqlSession.selectList("donationBoardMapper.selectModifyViewFile", boardNo);
	}

	@Override
	public int updateDonationBoard(DonationBoard db) {
		return sqlSession.update("donationBoardMapper.updateDonationBoard", db);
	}

	@Override
	public int deleteDonationBoardImage(int boardNo) {
		return sqlSession.delete("donationBoardMapper.deleteDonationBoardImage", boardNo);
	}

	@Override
	public User selectUserPoint(String userId) {
		return sqlSession.selectOne("userMapper.selectUserPoint", userId);
	}
}
