<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 수정하기"></c:set>
<%@ include file="../common/head.jspf"%>

<section class="article-section">

	<form action="doModify?id=${article.id}" method="post" class="article-form">
		<input type="hidden" name="id" value="${article.id}" />

		<div class="form-group">
			<label for="title">제목</label>
			<input type="text" id="title" name="title" value="${article.title}" required />
		</div>

		<div class="form-group">
			<label for="body">내용</label>
			<textarea id="body" name="body" rows="10" required>${article.body}</textarea>
		</div>

		<div class="form-group">
			<input type="submit" value="수정하기" />
		</div>
	</form>

	<div class="actions">
		<a href="detail?id=${article.id}" class="btn">취소</a>
	</div>
</section>


</body>
</html>