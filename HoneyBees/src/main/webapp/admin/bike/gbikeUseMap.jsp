<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	//谷歌地图API路径
	String googleApiUrl = "https://maps.googleapis.com";
	if ("zh_cn".equals(session.getAttribute("lang"))) {//中国大陆设置
		googleApiUrl = "http://maps.google.cn";
	}
%>
<div id="map" style="width:100%; height:100%"></div>

<script>
	var map;
	function initMap() {
		map = new google.maps.Map(document.getElementById('map'), {
			center : {
				lat : ${useVo.endLat},
				lng : ${useVo.endLng}
			},
			zoom : 20
		});

		//起点
		var smarker = new google.maps.Marker({
			position : {
				lat : ${useVo.startLat},
				lng : ${useVo.startLng}
			},
			map : map,
			icon : "image/bike_start_en_us.png"
		});

		//终点
		var emarker = new google.maps.Marker({
			position : {
				lat : ${useVo.endLat},
				lng : ${useVo.endLng}
			},
			map : map,
			icon : "image/bike_end_en_us.png"
		});

		//ride path
		var triangleCoords = new Array();
		<c:forEach items="${pathLine}" var="mapLL">
		var myLatLng = {
			lat : ${mapLL.latitude},
			lng : ${mapLL.longitude}
		};
		triangleCoords.push(myLatLng);

		</c:forEach>

		var bermudaTriangle = new google.maps.Polyline({
			path : triangleCoords,
			geodesic : true,
			strokeColor : '#FF0000',
			strokeOpacity : 1.0,
			strokeWeight : 2
		});

		bermudaTriangle.setMap(map);

		//city path
		var cityPathArr = new Array();
		<c:forEach items="${cityLine}" var="mapLL">
		var myLatLng = {
			lat : ${mapLL.latitude},
			lng : ${mapLL.longitude}
		};
		cityPathArr.push(myLatLng);

		</c:forEach>

		var cityPath = new google.maps.Polyline({
			path : cityPathArr,
			geodesic : true,
			strokeColor : '#FF0000',
			strokeOpacity : 1.0,
			strokeWeight : 2
		});

		cityPath.setMap(map);

		//parking path
		<c:forEach items="${areaLine}" var="area">

		var parkingArr = new Array();
		<c:forEach items="${area.path}" var="mapLL">
		var myLatLng = {
			lat : ${mapLL.latitude},
			lng : ${mapLL.longitude}
		};
		parkingArr.push(myLatLng);

		</c:forEach>

		var parkingPath = new google.maps.Polyline({
			path : parkingArr,
			geodesic : true,
			strokeColor : '${area.color}',
			strokeOpacity : 1.0,
			strokeWeight : 1
		});

		parkingPath.setMap(map);

		var parkingMarker = new google.maps.Marker({
			position : {
				lat : ${area.lat},
				lng : ${area.lng}
			},
			map : map,
			icon : "image/${area.icon}.png"
		});

		</c:forEach>

	}
</script>

<script
	src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap">
	
</script>