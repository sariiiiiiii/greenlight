<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>admin challengePage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/admin.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;200;300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
<!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function(){
      $(".header").load("header.html");
      $(".footer").load("footer.html");

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
    });
    function popUp() {
      window.open('/ChPopupView.do', '_blank', 
      'top=250, left=500, height=400, width=500,toolbar=no, menubar=no, location=no, status=no, scrollbars=no, resizable=no');
    }
  </script>
  <style>
  button {
  	float: right;
  	padding: 7px 12px 7px 12px;
  	margin: 20px 50px 0 0;
  	border: 1px solid rgba(128, 128, 128, 0.61);
  	border-radius: 5px;
  	font-weight: 300;
  	font-size: 14px;
	}
	button:hover {
  		background-color: #819789c4;
	}
  </style>
</head>
<body>
<jsp:include page="/common/header.jsp"></jsp:include>
  <div class="container">
    <div class="nav-admin">
      <i class="fas fa-user-circle fa-4x" style="color: gray; margin: 15px;"></i>
      <span>admin</span>
      <div style="margin-left: 40px; font-size: 14px; font-weight: 500; color:#7ea18b;">
        ????????????, ???????????????. <br>
        ????????? <span id="today" style="width:80px;"></span>??? ?????????.
      </div>
      <div class="nav-inner">
        <ul>
          <li class="li-area">
            <i class="fas fa-user-cog" style="margin-right: 10px;"></i><a href="userList.do">????????????</a>
          </li>
          <li class="li-area">
            <i class="fas fa-coins" style="margin-right: 10px;"></i><a href="#">????????????</a> 
            <ul>
              <li><a href="adminAuctionView.do">????????????</a></li>
              <li><a href="adminSellAuctionView.do">????????????</a></li>
            </ul>
          </li>
          <li class="li-area">
            <i class="far fa-calendar-check" style="margin-right: 10px;"></i><a href="AdminChList.do">????????? ??????</a> 
          </li>
          <li class="li-area">
            <i class="fas fa-hand-holding-usd" style="margin-right: 10px;"></i><a href="adminDonationBoardList.do">????????????</a>
          </li>
          <li class="li-area">
            <i class="fas fa-warehouse" style="margin-right: 10px;"></i><a href="#">????????????</a>
             <ul>
              <li><a href="adminOnlineShop.do">????????? ?????? ??????</a></li>
              <li><a href="adminOfflineShop.do">???????????? ?????? ??????</a></li>
            </ul>
          </li>
          <li class="li-area">
            <i class="fas fa-gifts" style="margin-right: 10px;"></i><a href="adminEventPage.do">???????????????</a> 
          </li>
        </ul>
      </div>
    </div>
      <div class="contents">
          <div class="con-title">
            <div>?????? ??? ??????</div>
            <button onclick="popUp();" value="????????? ????????????">????????? ??????</button>
            <p>???????????? ???????????? ???????????? ????????? ??????????????????.</p>
          </div>
          <div class="con-list">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>No</th>
                  <th>Title</th>
                  <th>Id</th>
                  <th>Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
              <c:forEach items="${cList }" var="challenge">
                <tr>
                  <td>${challenge.chNo }</td>
                  <c:url var="cDetail" value="ChallengeDetail.do">
                  	<c:param name="chNo" value="${challenge.chNo }"></c:param>
                  </c:url>
                  <td><a href="${cDetail }" style="text-decoration: none; color: black;">${challenge.chTitle }</a></td>
                  <td>${challenge.chWriter }</td>
                  <td>${challenge.writeDate }</td>
                  <td>
                  <c:if test="${challenge.chConfirm eq 'N' }">
                  	?????????
                  </c:if>
                  <c:if test="${challenge.chConfirm eq 'Y' }">
                  	??????
                  </c:if>
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
    <div class="page_wrap">
    <c:url var="before" value="AdminChList.do">
    	<c:param name="page" value="${api.currentPage - 1 }"></c:param>
    </c:url>
      <div class="page_nation">
      <c:if test="${api.currentPage <= 1 }">
         <a class="arrow prev" href="#"></a>
      </c:if>
      <c:if test="${api.currentPage > 1 }">
         <a class="arrow prev" href="${before }"></a>
      </c:if>
      <c:forEach var="p" begin="${api.startNavi}" end="${api.endNavi }">
      	<c:url var="pagenation" value="AdminChList.do">
      		<c:param name="page" value="${p }"></c:param>
      	</c:url>
      	<c:if test="${p eq api.currentPage }">
         	<a href="#" class="active">${p }</a>
      	</c:if>
      	<c:if test="${p ne api.currentPage }">
      		<a href="${pagenation }">${p }</a>
      	</c:if>
      </c:forEach>
      <c:url var="after" value="AdminChList.do">
      	<c:param name="page" value="${api.currentPage + 1 }"></c:param>
      </c:url>
      <c:if test="${api.currentPage >= api.maxPage }">
         <a class="arrow next" href="#"></a>
      </c:if>
      <c:if test="${api.currentPage < api.maxPage }">
         <a class="arrow next" href="${after }"></a>
      </c:if>
      </div>
      </div>
          </div>
      </div>
    </div>
  </div>
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
  </script>
<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>