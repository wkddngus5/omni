<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	    center: {lat: ${centerLat}, lng:${centerLng}},
	    zoom: 8,
	    styles: [
            {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
            {
              featureType: 'administrative.locality',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'geometry',
              stylers: [{color: '#263c3f'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'labels.text.fill',
              stylers: [{color: '#6b9a76'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry',
              stylers: [{color: '#38414e'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry.stroke',
              stylers: [{color: '#212a37'}]
            },
            {
              featureType: 'road',
              elementType: 'labels.text.fill',
              stylers: [{color: '#9ca5b3'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry',
              stylers: [{color: '#746855'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry.stroke',
              stylers: [{color: '#1f2835'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'labels.text.fill',
              stylers: [{color: '#f3d19c'}]
            },
            {
              featureType: 'transit',
              elementType: 'geometry',
              stylers: [{color: '#2f3948'}]
            },
            {
              featureType: 'transit.station',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'water',
              elementType: 'geometry',
              stylers: [{color: '#17263c'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.fill',
              stylers: [{color: '#515c6d'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.stroke',
              stylers: [{color: '#17263c'}]
            }
          ]
	  });
	  
	   <c:forEach items="${mapBikes}" var="mapBike">
	       var myLatLng = {lat: ${mapBike.lat}, lng: ${mapBike.lng}};
	       var marker = new google.maps.Marker({
			   position: myLatLng,
			   map: map,
			   title: "${mapBike.number}",
			   icon:"map_star1.png"
			});
	   
	   </c:forEach>
	   
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
	}
	
</script>
<script  src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap">   
</script>