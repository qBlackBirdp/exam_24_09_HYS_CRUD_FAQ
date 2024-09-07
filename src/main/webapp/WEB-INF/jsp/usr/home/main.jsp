<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Main"></c:set>
<%@ include file="../common/head.jspf"%>


<hr />

<section class="recent-articles">
    <h2>Recent Articles</h2>
    <ul>
        <c:forEach var="article" items="${recentArticles}">
            <li><a href="detail?id=${article.id}">${article.title}</a> - ${article.regDate.substring(0, 10)}</li>
        </c:forEach>
    </ul>
</section>


</body>
</html>