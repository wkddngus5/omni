<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	MessageFormat addSuccessMF = new MessageFormat(
			rb.getString("common_add_success_title"));

	//谷歌地图API路径
	String googleApiUrl = "https://maps.googleapis.com";
	if ("zh_cn".equals(session.getAttribute("lang"))) {//中国大陆设置
		googleApiUrl = "http://maps.google.cn";
	}
	String funcId = "";
	if(request.getAttribute("funcId") != null){
		funcId = request.getAttribute("funcId").toString();
	}
	String actionUrl = "areaManage?requestType=80007&funcId=" + funcId;
%>
<head>
<link href="themes/default/style.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/core.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/print.css" rel="stylesheet" type="text/css"
	media="print" />
</head>
<body>

	<div class="pageHeader">
		<form id="pageForm" onsubmit="return navTabSearch(this);"
			class="pageForm required-validate" action="<%=actionUrl%>"
			method="post">
			<div class="searchBar">
				<table class="searchContent">

					<tr>
						<td><%=rb.getString("common_name_title")%>：</td>
						<td><input type="text" name="name" id="name"
							value="${cityVo.name}" class="required" maxlength="50"/></td>
					</tr>
					<tr>
						<td><%=rb.getString("common_note_title")%>：</td>
						<td><input type="text" name="note" id="note"
							value="${cityVo.note}" maxlength="50"/></td>
					</tr>

					<input type="hidden" name="id" value="${cityVo.id}" />
										<tr hidden="hidden">
						<td>
						<input type="text" id="detail" name="detail"
							value="${cityVo.area_detail}" class="required"
							readonly="readonly" />
						<input type="hidden" name="area_lat" id="area_lat" value="${cityVo.area_lat}"/>
						<input type="hidden" name="area_lng" id="area_lng" value="${cityVo.area_lng}"/>
							</td>
					</tr>

				</table>
				<div class="subBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button"  onclick="clearnPath()"><%=rb.getString("common_clearn_title")%></button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" onclick="autoCircle()"><%=rb.getString("bike_area_auto_circle")%></button>
								</div>
							</div></li>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit" onclick="submitForm()"><%=rb.getString("common_commit_title")%></button>
								</div>
							</div></li>
					</ul>
				</div>
			</div>
		</form>
	</div>

	<div id="map" style="width:100%; height:80%"></div>
	<script>
		//var infoWindow; 
		var map;
		var polyPath;
		// var coords=new Array();
		var centerMarker;

		
		
		function initMap() {
		
		//加载已选择区域
		var triangleCoords = new Array();
			<c:forEach items="${latLngList}" var="mapLL">
			var myLatLng = {
				lat : ${mapLL.latitude},
				lng : ${mapLL.longitude}
			};
			triangleCoords.push(myLatLng);

			</c:forEach>
		
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : ${centerLat},
					lng : ${centerLng}
				},
				zoom : 15,
			});

			createPolyPath(triangleCoords);
			getCenter();
			google.maps.event.addListener(map, 'click', function(event) {
				if(polyPath == null){
					createPolyPath(null);
				}
				if(polyPath.getMap() == null){
					polyPath.setMap(map);
				}
				addLatLngToPoly(event.latLng, polyPath);

			});
				google.maps.event.addListener(map, 'dragend', function(event) {
				getCenter();
			});
		}

		function addLatLngToPoly(latLng, poly) {
			var path = poly.getPath();

			path.push(latLng);

			var encodeString = google.maps.geometry.encoding.encodePath(path);
			if (encodeString) {
				document.getElementById('detail').value = encodeString;
			}

		}
		
		function createPolyPath(paths){
			polyPath = new google.maps.Polyline({
				path:paths,
				strokeColor : '#FF0000',
				strokeOpacity : 1.0,
				strokeWeight : 2,
				map : map
			});
		}
		
		//清空路径
		function clearnPath(){
			polyPath.setMap(null);
			polyPath = null;
			document.getElementById('detail').value = "";
		}
		//自动封闭
		function autoCircle(){
			if(polyPath.getPath().length > 2){
				addLatLngToPoly(polyPath.getPath().getArray()[0], polyPath);
			}
			
		}
		//get map center
		function getCenter(){
			if(centerMarker != null){
				centerMarker.setMap(null);
			}
	       	centerMarker = new google.maps.Marker({
			   position: map.center,
			   map: map,
			   title: "Center",
			});
			var center = map.center.toString().replace("(", "").replace(")", "").split(",");
			document.getElementById('area_lat').value = center[0];
			document.getElementById('area_lng').value = center[1];
		}
	</script>
	<script
		src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap">
		
	</script>
	<script type="text/javascript" src="dwz/js/dwz-ajax.js"></script>
	<script type="text/javascript">
		//提交
		function submitForm() {
			htmlObj = $.ajax({
				cache : true,
				type : "POST",
				url : "<%=actionUrl%>",
				data : $("#pageForm").serialize(),
				async : false
			});
			var obj = jQuery.parseJSON(htmlObj.responseText);
			if (obj.statusCode != 200) {
				alertMsg.error(obj.message, null);
			} else {
				navTab.reloadFlag("bike_city_list");
				navTab.closeCurrentTab();
				alertMsg.correct(obj.data, null);
			}

		}
	</script>
</body>
