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
window.onscroll = function() {
	
	if((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
	
		$.ajax({
			type: "post",
			url: "restaurantList.re?pageNum=${pageInfo.pageNum + 1}",
			data: $("#list").serialize(),
			dataType: "text",
			success: 
				function(response) {
					if(${pageInfo.endPage} == ${pageInfo.pageNum + 1}) {
						$("#writeForm").remove();
						$("#header").remove();
						$("#footer").remove();
						$("#append").html(response);
					
					}
				}
		});
	}
}

</script>
<style>
	.star-rating {width:205px; }
	.star-rating,.star-rating span {display:inline-block; height:39px; overflow:hidden; background:url(image/star3.png)no-repeat; }
	.star-rating span{background-position:left bottom; line-height:0; vertical-align:top; }
</style>
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
		
		<c:choose>
			<c:when test="${empty restaurantInfo }">
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
	<button onclick="location.href='restaurantWriteForm.re'">글쓰기</button>
	<header id="header"> <!--헤더라인 -->
<%-- 		<jsp:include page="../inc/"> --%>
			<hr>
				<h1>헤더라인</h1>
			<hr>
	</header>

	<section id="list">
		<c:choose>
			<c:when test="${not empty restaurantInfo and pageInfo.listCount gt 0}">
				<c:forEach var="resInfo" items="${restaurantInfo}" varStatus="state">
					<!--  -->
					<table id="listView">
						<tr><td>${resInfo.resName}</td></tr>
						<tr><th><img height="200px"src="upload/${resInfo.photo }" alt="파일"></th></tr>
						<tr><td><span id="result${state.count }"></span></td></tr>
						<tr><td id="result${state.count }"></td></tr>
						<tr><td> <div class='star-rating'><span style ="width:${resInfo.rating*20}%"></span></div></td><td id="rating">${resInfo.rating }점</td></tr>
						<tr><td><span>&#9829;</span>${board.likes} 개</td></tr>				
<%-- 						<tr><td id="content${state.count }" onclick="location.href='ReviewDetail.re?idx=' + ${board.idx}+'&pageNum=' +${pageInfo.pageNum}">${board.content}</td></tr> <!-- 이부분에서 나중에 댓글 항목 추가, 더보기 란 할 수 있도록 해야함 --> --%>
					</table>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="8">게시글이 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</section>
	<section id="append">
	</section>

	<footer id="footer"> <!--푸터라인 -->
				<hr>
				<h1>푸터라인</h1>
				<hr>
<%-- 		<jsp:include page="../inc/"> --%>
	</footer>
</body>
</html>