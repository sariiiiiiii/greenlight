<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>admin Auction</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/admin.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;200;300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
<!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<jsp:include page="/common/header.jsp"></jsp:include>
<div class="container">
    <div class="nav-admin">
      <i class="fas fa-user-circle fa-4x" style="color: gray; margin: 15px;"></i>
      <span>admin</span>
      <div style="margin-left: 40px; font-size: 14px; font-weight: 500; color:#7ea18b;">
        관리자님, 안녕하세요. <br>
        오늘은 <span id="today" style="width:80px;"></span>일 입니다.
      </div>
      <div class="nav-inner">
        <ul>
          <li class="li-area">
            <i class="fas fa-user-cog" style="margin-right: 10px;"></i><a href="userList.do">회원관리</a>
          </li>
          <li class="li-area">
            <i class="fas fa-coins" style="margin-right: 10px;"></i><a href="#">경매관리</a> 
            <ul>
              <li><a href="adminAuctionView.do">재고관리</a></li>
              <li><a href="adminSellAuctionView.do">판매관리</a></li>
            </ul>
          </li>
          <li class="li-area">
            <i class="far fa-calendar-check" style="margin-right: 10px;"></i><a href="AdminChList.do">챌린지 관리</a> 
          </li>
          <li class="li-area">
            <i class="fas fa-hand-holding-usd" style="margin-right: 10px;"></i><a href="adminDonationBoardList.do">기부관리</a>
          </li>
          <li class="li-area">
            <i class="fas fa-warehouse" style="margin-right: 10px;"></i><a href="#">매장관리</a>
             <ul>
              <li><a href="adminOnlineShop.do">온라인 매장 관리</a></li>
              <li><a href="adminOfflineShop.do">오프라인 매장 관리</a></li>
            </ul>
          </li>
          <li class="li-area">
            <i class="fas fa-gifts" style="margin-right: 10px;"></i><a href="adminEventPage.do">이벤트관리</a> 
          </li>
        </ul>
      </div>
    </div>
      <div class="contents">
          <div class="con-title">
        <form action="userPayPoint.do" method="post">
            <div>판매승인관리</div>
            <button type="submit" value="판매승인">판매승인</button>
            <p>상품을 최종 낙찰한 회원들의 포인트를 회수해주세요.</p>
          </div>
          <div class="con-list">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>No</th>
                  <th>물품명</th>
                  <th>낙찰가</th>
                  <th>판매자</th>
                  <th>구매자</th>
                  <th>낙찰일</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
              <c:if test="${ empty sList }">
              	<tr>
                  <td colspan="7" align="center">낙찰된 경매가 없습니다.</td>
	            </tr>
              </c:if>
              <c:if test="${ not empty sList }">
	              <c:forEach items="${sList }" var="auction" varStatus="status">
	                <tr>
	                  <td>${auction.count }</td>
	                  <td>${auction.auctionTitle }</td>
	                  <td>${auction.auctionPrice }</td>
	                  <td>${auction.seller }</td>
	                  <td>${auction.buyer }</td>
	                  <td>${auction.auctionDate }</td>
	                  <td><input type="checkbox" value="${auction.auctionNo }" name="auctionNo"></td>
	                </tr>
	              </c:forEach>
              </c:if>
              </tbody>
            </table>
            </form>
            
            <div class="page_wrap">
			    <c:url var="before" value="adminSellAuctionView.do">
			    	<c:param name="page" value="${pi.currentPage - 1 }"></c:param>
			    </c:url>
			      <div class="page_nation">
			      <c:if test="${pi.currentPage <= 1 }">
			         <a class="arrow prev" href="#"></a>
			      </c:if>
			      <c:if test="${pi.currentPage > 1 }">
			         <a class="arrow prev" href="${before }"></a>
			      </c:if>
			      <c:forEach var="p" begin="${pi.startNavi}" end="${pi.endNavi }">
			      	<c:url var="pagenation" value="adminSellAuctionView.do">
			      		<c:param name="page" value="${p }"></c:param>
			      	</c:url>
			      	<c:if test="${p eq pi.currentPage }">
			         	<a href="#" class="active">${p }</a>
			      	</c:if>
			      	<c:if test="${p ne pi.currentPage }">
			      		<a href="${pagenation }">${p }</a>
			      	</c:if>
			      </c:forEach>
			      <c:url var="after" value="adminSellAuctionView.do">
			      	<c:param name="page" value="${pi.currentPage + 1 }"></c:param>
			      </c:url>
			      <c:if test="${pi.currentPage >= pi.maxPage }">
			         <a class="arrow next" href="#"></a>
			      </c:if>
			      <c:if test="${pi.currentPage < pi.maxPage }">
			         <a class="arrow next" href="${after }"></a>
			      </c:if>
			      </div>
			 </div>
           </div>
          </div>
      </div>
    </div>
</div>
<jsp:include page="/common/footer.jsp"></jsp:include>
<script>
	todayDate();

	function todayDate(){
	  var today = new Date();
	
	  var year = today.getFullYear();
	  var month = ('0' + (today.getMonth() + 1)).slice(-2);
	  var day = ('0' + today.getDate()).slice(-2);
	
	  var dateString = year + '.' + month  + '.' + day;
	
	  $("#today").html(dateString);
	}
	
	$('div > ul > li').click(function() {
        if ( $(this).hasClass('active') ) {
          $(this).find(' > ul').stop().slideUp(300);
          $(this).removeClass('active');
        }
        else {
          $(this).find(' > ul').stop().slideDown(300);
          $(this).addClass('active');
        }
      });
</script>
</body>
</html>