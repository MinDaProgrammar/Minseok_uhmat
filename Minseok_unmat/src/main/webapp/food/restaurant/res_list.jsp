<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="js/jquery-3.6.0.js"></script>
<script>
	//스크롤 이벤트 발생
	window.onscroll = function() {
	
		if((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
			alert("바닥에 닿음");
			$.ajax({
				type: "post",
				url: "restaurantList.re?pageNum=${pageInfo.pageNum + 1}",
				data: {
					startPage : ${pageInfo.startPage +1}
				},
				dataType: "text",
				
				success: 
					function(response) {
						if(${pageInfo.endPage} >= ${pageInfo.pageNum + 1}) {
							var content = $(response).find("#repeat");
							$("table").eq(1).append(content);
						}
					},
			});
		} 
	}

</script>
</head>
<body>
	<h1>restaurantList Page</h1>
	
	<table border="1">
		<tr>
			<th> 식당이름 </th>
			<th> 별점 </th>
			<th> 리뷰 개수</th>
			<th> 사진 </th>
		</tr>
	</table>
	<section id="repeat">
	<table border="1">
		<c:choose>
			<c:when test="${empty restaurantInfo  and pageInfo.listCount le 0 }">
				<tr>
					<td colspan="4">
						게시된 식당이 없습니다.
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${restaurantInfo }" var="resInfo">
					<tr onclick="location.href='restaurantDetail.re?resName=${resInfo.resName}'">
						<td>${resInfo.resName }</td>
						<td>${resInfo.rating }</td>
						<td>${resInfo.reviewCount }</td>
						<td><img width="200" src="upload/${resInfo.photo }"></td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	</section>
	
	<button onclick="location.href='restaurantWriteForm.re'">글쓰기</button>
	<button onclick="location.href='resCategory.re'">카테고리 보기</button>
	<button onclick="location.href='index.jsp'">홈으로</button>
</body>
</html>