<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="${board.code} 목록"></c:set>
<%@ include file="../common/head.jspf"%>

<hr />

<table class="article-table">
	<thead>
		<tr>
			<th>ID</th>
			<th>Registration Date</th>
			<th>Title</th>
			<th>Writer</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${empty articles}">
			<tr>
				<td colspan="4" style="text-align: center;">게시글이 없습니다</td>
			</tr>
		</c:if>
		<c:if test="${not empty articles}">
			<c:forEach var="article" items="${articles}">
				<tr>
					<td>${article.id}</td>
					<td>${article.regDate.substring(0, 10)}</td>
					<td><a href="detail?id=${article.id}">${article.title}</a> <c:if
							test="${article.extra__repliesCount > 0 }">
							<span style="color: red;">[${article.extra__repliesCount }]</span>
						</c:if>
					<td>${article.extra__writer}</td>
					<td style="text-align: center;">${article.sumReactionPoint}</td>
					<td style="text-align: center;">${article.goodReactionPoint}</td>
					<td style="text-align: center;">${article.badReactionPoint}</td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>

<c:if test="${rq.isLogined()}">
	<div
		style="text-align: right; margin-bottom: 10px; margin-right: 20px;">
		<a href="write" class="btn">게시물 작성하기</a>
	</div>
</c:if>

<c:if test="${totalPages > 0}">
	<div class="pagination">
		<c:set var="baseUri" value="?boardId=${boardId}" />
		<c:if
			test="${searchField != null && !searchField.isEmpty() && searchKeyword != null && !searchKeyword.isEmpty()}">
			<c:set var="baseUri"
				value="${baseUri}&searchField=${searchField}&searchKeyword=${searchKeyword}" />
		</c:if>

		<c:if test="${currentPage > 1}">
			<a href="${baseUri}&page=${currentPage - 1}" class="btn">Previous</a>
		</c:if>

		<c:choose>
			<c:when test="${currentPage == 1}">
				<span class="current">1</span>
			</c:when>
			<c:otherwise>
				<a href="${baseUri}&page=1" class="btn">1</a>
			</c:otherwise>
		</c:choose>

		<c:forEach var="i" begin="${currentPage > 3 ? currentPage - 2 : 2}"
			end="${currentPage + 2 > totalPages ? totalPages - 1 : currentPage + 2}">
			<c:choose>
				<c:when test="${i == currentPage}">
					<span class="current">${i}</span>
				</c:when>
				<c:otherwise>
					<a href="${baseUri}&page=${i}" class="btn">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:choose>
			<c:when test="${currentPage == totalPages}">
				<span class="current">${totalPages}</span>
			</c:when>
			<c:otherwise>
				<a href="${baseUri}&page=${totalPages}" class="btn">${totalPages}</a>
			</c:otherwise>
		</c:choose>

		<c:if test="${currentPage < totalPages}">
			<a href="${baseUri}&page=${currentPage + 1}" class="btn">Next</a>
		</c:if>
	</div>
</c:if>

<div class="search-bar">
	<form action="/usr/article/list" method="GET">
		<div class="search-container">
			<select name="searchField" class="search-field-select"
				data-value="${searchField}">
				<option value="title" ${searchField == 'title' ? 'selected' : ''}>제목</option>
				<option value="body" ${searchField == 'body' ? 'selected' : ''}>내용</option>
				<option value="extra__writer"
					${searchField == 'extra__writer' ? 'selected' : ''}>작성자</option>
			</select> <input type="text" name="searchKeyword" class="search-input"
				placeholder="검색어를 입력하세요" value="${searchKeyword}">

			<button type="submit" class="search-button">검색</button>
		</div>

		<input type="hidden" name="boardId" value="${boardId}"> <input
			type="hidden" name="page" value="1">
	</form>
</div>

<style>
.current {
	background-color: #1b7d6d;
	color: #ffffff;
	padding: 8px 16px;
	border-radius: 5px;
	margin: 0 2px;
}

.hidden {
	display: none;
}

.dots {
	cursor: pointer;
	position: relative;
}

.page-input {
	position: absolute;
	background-color: #1e1e1e;
	border: 1px solid #333;
	padding: 10px;
	border-radius: 4px;
	margin-top: 5px;
	z-index: 1000;
}

.page-input input[type="number"] {
	width: 50px;
	margin-right: 10px;
}

.page-input button {
	padding: 5px 10px;
	background-color: #2a9d8f;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.page-input button:hover {
	background-color: #219080;
}
</style>

</body>
</html>
