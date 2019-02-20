<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	//谷歌地图API路径
	String googleApiUrl = "https://maps.googleapis.com";
	if ("zh_cn".equals(session.getAttribute("lang"))) {//中国大陆设置
		googleApiUrl = "http://maps.google.cn";
	}

%>
<body>
	<div id="map" style="width:100%; height:100%"></div>
	<script>
		var infoWindow;
		var map;
		function initMap() {
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : ${centerLat},
					lng : ${centerLng}
				},
				zoom : ${mapZoom},
				//mapTypeId : 'terrain'

			});

			var triangleCoords = new Array();
			<c:forEach items="${latLngList}" var="mapLL">
			var myLatLng = {
				lat : ${mapLL.latitude},
				lng : ${mapLL.longitude}
			};
			triangleCoords.push(myLatLng);

			</c:forEach>

			var bermudaTriangle = new google.maps.Polygon({
				paths : triangleCoords,
				strokeColor : '#FF0000',
				strokeOpacity : 0.8,
				strokeWeight : 3,
				fillColor : '#FF0000',
				fillOpacity : 0.35
			});
			bermudaTriangle.setMap(map);
			bermudaTriangle.addListener('click', showMsg);

			infoWindow = new google.maps.InfoWindow;
			
			
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

		function showMsg(event) {

			var vertices = this.getPath();
			var xy = vertices.getAt(0);

			var contentString = 'area name: "${area.name}" <br>'
					+ 'area note: "${area.note}" <br>'
			infoWindow.setContent(contentString);
			infoWindow.setPosition(xy);
			infoWindow.open(map);

		}
	</script>
	<script
		src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap">
	</script>
</body>
