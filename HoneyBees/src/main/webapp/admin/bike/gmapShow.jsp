<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	//谷歌地图API路径
	String googleApiUrl = "https://maps.googleapis.com";
	if ("zh_cn".equals(session.getAttribute("lang"))) {//中国大陆设置
		googleApiUrl = "http://maps.google.cn";
	}
%>
<div id="map" style="width:100%; height:100%"></div>
 <!--[if gte IE 9]><!-->
<script src="dwz/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<!--<![endif]-->
<script type="text/javascript">
 
	var map;
	var infowindow;

	function initMap() {
	  map = new google.maps.Map(document.getElementById('map'), {
	    center: {lat: ${centerLat}, lng:${centerLng}},
	   	<c:if test="${only==true}">zoom:15</c:if>//单个显示，提高缩放级别
	    <c:if test="${only==null}">zoom:8</c:if>
	  });
	  
	    infowindow = new google.maps.InfoWindow({
      
        });
	  
	   <c:forEach items="${mapBikes}" var="mapBike">
	       var myLatLng = {lat: ${mapBike.lat}, lng: ${mapBike.lng}};
	       var marker = new google.maps.Marker({
			   position: myLatLng,
			   map: map,
			   title: "${mapBike.number}",
			   icon:"${mapBike.icon}",
			   bid:"${mapBike.bid}"
			});
			attachEvent(marker);
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
	   function attachEvent(marker) {
            google.maps.event.addListener(marker, 'click', function (){                
                showInfoWindow(marker);
             });
		}
	   function showInfoWindow(marker) {
            infowindow.open(map, marker);
            loadBikeInfo(marker.bid); 
        }
        
      //加载单车信息
		function loadBikeInfo(bid) {
			htmlObj = $.ajax({
				cache : true,
				type : "get",
				url : "bikeManage?requestType=20042&id="+bid,
				//data : $("#pageForm").serialize(),
				async : false
			});
			var obj = jQuery.parseJSON(htmlObj.responseText);
			if (obj.statusCode != 200) {
				//alertMsg.error(obj.message, null);
				alert(obj.message);
			} else {
				var content = '<div>'+obj.data+'</div>';
				infowindow.setContent(content);
			}

		}
		
</script>
<script
	src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap">
</script>
