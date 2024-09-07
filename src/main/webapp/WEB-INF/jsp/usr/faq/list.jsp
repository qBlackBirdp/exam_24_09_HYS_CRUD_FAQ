<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="FAQ"></c:set>
<%@ include file="../common/head.jspf"%>

<hr />

<!-- 검색 영역 -->
<div class="search-bar">
    <form action="/usr/faq/list" method="get">
        <div class="search-container">
            <select class="search-field-select" name="searchField">
                <option value="question">질문</option>
                <option value="answer">답변</option>
            </select>
            <input class="search-input" type="text" name="keyword" placeholder="검색어 입력">
            <button class="search-button" type="submit">검색</button>
        </div>
    </form>
</div>

<!-- FAQ 목록 테이블 -->
<div>
    <!-- 검색어가 있을 경우 메시지 출력 -->
    <c:if test="${keyword != ''}">
        <p>검색어: "${keyword}"</p>
    </c:if>
    <!-- 검색된 결과가 없는 경우 -->
    <c:if test="${faqsCount == 0}">
        <tr>
            <td colspan="3" style="text-align: center;">"${keyword}"에 대한 정보는 없습니다.</td>
        </tr>
    </c:if>
</div>

<table class="article-table">
    <thead>
    <tr>
        <th>질문</th>
        <th>답변</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="faq" items="${faqs}">
        <tr>
            <td>${faq.question}</td>
            <td>${faq.answer}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<c:if test="${pagesCount > 1}">
    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="?page=${currentPage - 1}&keyword=${keyword}">이전</a>
        </c:if>

        <c:forEach var="i" begin="1" end="${pagesCount}">
            <c:choose>
                <c:when test="${i == currentPage}">
                    <span>${i}</span>
                </c:when>
                <c:otherwise>
                    <a href="?page=${i}&keyword=${keyword}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage < pagesCount}">
            <a href="?page=${currentPage + 1}&keyword=${keyword}">다음</a>
        </c:if>
    </div>
</c:if>


</body>
</html>
