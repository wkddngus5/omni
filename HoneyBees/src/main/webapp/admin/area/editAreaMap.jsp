<%@page import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
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
	if (request.getAttribute("funcId") != null) {
		funcId = request.getAttribute("funcId").toString();
	}

	String[] areaType = rb.getString("bike_area_type").split(",");//get area type

	String actionUrl = "areaManage?requestType=80002&funcId=" + funcId;
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
							value="${areaVo.name}" class="required" maxlength="50"/></td>
					</tr>
					<tr>
						<td><%=rb.getString("common_note_title")%>：</td>
						<td><input type="text" name="note" id="note"
							value="${areaVo.note}" class="required" maxlength="50"/></td>
					</tr>

					<tr>
						<td><%=rb.getString("common_type_title")%>：</td>
						<td><input type="radio" name="type" value="1"
							<c:if test="${areaVo == null || areaVo.type == 1}">checked="checked"</c:if> /><%=areaType[1]%>
							<input type="radio" name="type"
							<c:if test="${areaVo.type == 2}">checked="checked"</c:if>
							value="2" /><%=areaType[2]%> <input type="radio" name="type"
							<c:if test="${areaVo.type == 3}">checked="checked"</c:if>
							value="3" /><%=areaType[3]%></td>
					</tr>

					<tr>

						<input type="hidden" name="id" value="${areaVo.id}" />
					<tr hidden="hidden">
						<td><input type="text"  id="detail" name="detail"
							value="${areaVo.detail}" class="required" readonly="readonly" /></td>
					</tr>

					<c:if test="${admin == 1}">
						<tr>
							<td><%=rb.getString("user_area_title")%>：</td>
							<td>
								<select name="cityId">
									<option value="0"><%=rb.getString("common_all_title")%></option>
									<c:forEach items="${cityList}" varStatus="status" var="city">
										<option value="${city.id}"
											<c:if test="${city.id == tagCityId}">selected="selected"</c:if>>
										${city.name}
										</option>
									</c:forEach>

							</select>
							</td>

						</tr>
					</c:if>

				</table>


				<div class="subBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" onclick="clearnPath()"><%=rb.getString("common_clearn_title")%></button>
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
	<script type="text/javascript">
		//var infoWindow; 
		var map;
		var polyPath;
		// var coords=new Array();
		

		
		
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

			google.maps.event.addListener(map, 'click', function(event) {
				if(polyPath == null){
					createPolyPath(null);
				}
				if(polyPath.getMap() == null){
					polyPath.setMap(map);
				}
				addLatLngToPoly(event.latLng, polyPath);

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
	</script>
	<script
		src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap"></script>

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
				navTab.reloadFlag("area_list");
				navTab.closeCurrentTab();
				alertMsg.correct(obj.data, null);
			}

		}
	</script>
</body>
