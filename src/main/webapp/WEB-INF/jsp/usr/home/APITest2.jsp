<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="API TEST"></c:set>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>지도 생성하기</title>
    
</head>
<body>
<!-- 지도를 표시할 div 입니다 -->
<div id="map" style="width:100%;height:350px;"></div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=6cd6b3eb36bdca73d43a17e61f8437fe"></script>
<script>
const API_KEY = '%2BKApk0ihIn7Any5BM3X7%2BnlJyc2yXA39m%2F%2BXn4uiAkLPB7wbUQENd3NzoFktNEchJlI1pyRmWXXZJhA%2FTNfeIg%3D%3D';

var lat;
var lot;

async function getCData2() {
	const url = 'https://apis.data.go.kr/6300000/openapi2022/tasuInfo/gettasuInfo?pageNo=1&numOfRows=10&serviceKey='
			+ API_KEY;
	const response = await
	fetch(url);
	const data = await
	response.json();
	console.log("data", data);
	console.log("data.response", data.response);
	console.log("data.response.body", data.response.body);
	console.log("data.response.body.items", data.response.body.items);
	console.log("data.response.body.items[0]", data.response.body.items[0]);
	console.log("data.response.body.items[0].loCrdnt",
			data.response.body.items[0].loCrdnt);
	console.log("data.response.body.items[0].laCrdnt",
			data.response.body.items[0].laCrdnt);
	lat = data.response.body.items[0].laCrdnt;
	lot = data.response.body.items[0].loCrdnt;
}
getCData2();
var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
        center: new kakao.maps.LatLng(lat, lot), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption); 
</script>
</body>
</html>

<%@ include file="../common/foot.jspf"%>