<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 작성하기"></c:set>
<%@ include file="../common/head.jspf"%>

<section class="article-section">

	<form action="doWrite?id=${article.id}" method="post" class="article-form">
		<input type="hidden" name="id" value="${article.id}" />
		
		<div class="form-group">
			<label for="boardId">게시판 선택</label>
			<select id="boardId" name="boardId" class="form-select" required>
				<option value="">게시판을 선택하세요</option>
				<option value="1">Notice</option>
				<option value="2">Free</option>
				<option value="3">QnA</option>
			</select>
		</div>

		<div class="form-group">
			<label for="title">제목</label>
			<input type="text" id="title" name="title" value="${article.title}" required />
		</div>

		<div class="form-group">
			<label for="body">내용</label>
			<textarea id="body" name="body" rows="10" required>${article.body}</textarea>
		</div>

		<div class="form-group">
			<input type="submit" value="작성하기" />
		</div>
	</form>

	<div class="actions">
		<a href="list" class="btn">취소</a>
	</div>
</section>


</body>
</html>