<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>challengeModifyForm</title>
<script src="${pageContext.request.contextPath}/resources/js/summernote/lang/summernote-ko-KR.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/challenge/writeForm.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;200;300;400;500;600;700;900&display=swap" rel="stylesheet">
<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
</head>
<script>
$(document).ready(function(){

    $('#summernote').summernote({
		  height: 400,                 		// 에디터 높이
		  minHeight: null,             		// 최소 높이
		  maxHeight: 600,             		// 최대 높이
		  focus: true,                  	// 에디터 로딩후 포커스를 맞출지 여부
		  lang: "ko-KR",					// 한글 설정
		  placeholder: '내용을 입력해주세요.'		// placeholder 설정
        
	});
  });
</script>
<body>
<jsp:include page="/common/header.jsp"></jsp:include>
  <div class="container">
    <form action="ChallengeUpdate.do" method="post" enctype="multipart/form-data">
    <input type="hidden" value="${challenge.categoryNo }" name="categoryNo">
    <input type="hidden" value=${challenge.chNo } name="chNo" id="chNo">
      <div class="write-form">
        <span class="write-left">챌린지 참여하기</span>
        <span class="write-right">하루 한장, 모바일 영수증 받기!</span>
	        <div class="title-area">
	          <input type="text" name="chTitle" value="${challenge.chTitle }">
	        </div>
	        <textarea name="editordata" id="summernote">${challenge.chContents }</textarea>
	        <!-- <div>
	          <p>사진을 업로드 해주세요.</p>
	          <input type="file" name="uploadFile" id="img" accept="image/*" onchange="setThumbnail(event);"/>
	          <div class="image_container"></div>
	        </div> -->
	        <div id='image_preview'>
	          <h3>사진을 업로드 해주세요.</h3>
	          <input type='file' id='btnAtt' multiple='multiple' name="uploadFile"/>
	          <div id='att_zone' 
	                data-placeholder='파일을 첨부 하려면 파일 선택 버튼을 클릭하거나 파일을 드래그앤드롭 하세요'>
	          	<c:forEach items="${cList }" var="chFile">
	          		<div style="display:inline-block;position:relative;width:150px;height:150px;margin:5px;z-index:1">
          				<img style="width:100%;height:100%;z-index:none" src="${pageContext.request.contextPath }/resources/cuploadFiles/${chFile.fileName }">
          				<input type="button" onclick="deleteFile(this);"value="x" delfile="${chFile.fileName }" style="width:30px;height:30px;position:absolute;font-size:24px;right:0px;bottom:0px;z-index:999;background-color:rgba(255,255,255,0.1);color:#f00">
          			</div>
	          	</c:forEach>
	          </div>
	        </div>
	      </div>
	      <input class="submitBtn" type="submit" value="수정">
    </form>
  </div>
 <script>
 	function test() {
 		console.log($("#chNo").val());
 	}
    ( /* att_zone : 이미지들이 들어갈 위치 id, btn : file tag id */
      imageView = function imageView(att_zone, btn){
    
        var attZone = document.getElementById(att_zone);
        var btnAtt = document.getElementById(btn)
        var sel_files = [];
        
        // 이미지와 체크 박스를 감싸고 있는 div 속성
        var div_style = 'display:inline-block;position:relative;'
                      + 'width:150px;height:150px;margin:5px;z-index:1;border:0;';
        // 미리보기 이미지 속성
        var img_style = 'width:100%;height:100%;z-index:none';
        // 이미지안에 표시되는 체크박스의 속성
        var chk_style = 'width:30px;height:30px;position:absolute;font-size:15px;'
                      + 'right:0px;bottom:0px;z-index:999;background-color:rgba(255,255,255,0.1);color:#f00';
      
        btnAtt.onchange = function(e){
          var files = e.target.files;
          var fileArr = Array.prototype.slice.call(files)
          for(f of fileArr){
            imageLoader(f);
          }
        }  
        
        // 탐색기에서 드래그앤 드롭 사용
        attZone.addEventListener('dragenter', function(e){
          e.preventDefault();
          e.stopPropagation();
        }, false)
        
        attZone.addEventListener('dragover', function(e){
          e.preventDefault();
          e.stopPropagation();
          
        }, false)
      
        attZone.addEventListener('drop', function(e){
          var files = {};
          e.preventDefault();
          e.stopPropagation();
          var dt = e.dataTransfer;
          files = dt.files;
          for(f of files){
            imageLoader(f);
          }
          
        }, false)
        
        /*첨부된 이미지를 배열에 넣고 미리보기 */
        imageLoader = function(file){
          sel_files.push(file);
          var reader = new FileReader();
          reader.onload = function(ee){
            let img = document.createElement('img')
            img.setAttribute('style', img_style)
            img.src = ee.target.result;
            attZone.appendChild(makeDiv(img, file));
          }
          
          reader.readAsDataURL(file);
        }
        
        /*첨부된 파일이 있는 경우 checkbox와 함께 attZone에 추가할 div를 만들어 반환 */
        makeDiv = function(img, file){
          var div = document.createElement('div')
          div.setAttribute('style', div_style)
          
          var btn = document.createElement('input')
          btn.setAttribute('type', 'button')
          btn.setAttribute('value', 'x')
          btn.setAttribute('delFile', file.name);
          btn.setAttribute('style', chk_style);
          btn.onclick = function(ev){
            var ele = ev.srcElement;
            var delFile = ele.getAttribute('delFile');
            for(var i=0 ;i<sel_files.length; i++){
              if(delFile== sel_files[i].name){
                sel_files.splice(i, 1);      
              }
            }
            
            dt = new DataTransfer();
            for(f in sel_files) {
              var file = sel_files[f];
              dt.items.add(file);
            }
            btnAtt.files = dt.files;
            var p = ele.parentNode;
            attZone.removeChild(p)
          };
          div.appendChild(img)
          div.appendChild(btn)
          return div
        }
      }
    )('att_zone', 'btnAtt')
    
    function deleteFile(data){
    	$(data).parent().remove();
    }
    </script>
<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>