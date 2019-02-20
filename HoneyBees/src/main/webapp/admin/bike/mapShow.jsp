<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
 <!--[if gte IE 9]><!-->
<script src="dwz/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<!--<![endif]-->
<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=80ed3318204c5bc6744f6d33c8a59ed5"></script>
<script type="text/javascript"
	src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
<div id="container" style="width:100%; height:100%"></div>

<script>
	var map = new AMap.Map('container', {
		resizeEnable : true,
		 	<c:if test="${only==true}">zoom:18</c:if>//单个显示，提高缩放级别
	    <c:if test="${only==null}">zoom:8</c:if>,
		center : [ "${centerLng}", "${centerLat}" ]
	});
	var infoWindow = new AMap.InfoWindow({
		offset : new AMap.Pixel(0, -30)
	});

	<c:forEach items="${mapBikes}" var="mapBike">
	var marker = new AMap.Marker({
		map : map,
		position : [ "${mapBike.lng}", "${mapBike.lat}" ],
		icon : new AMap.Icon({
			size : new AMap.Size(40, 50), //图标大小
			image : "${mapBike.icon}",
			
		}),
		title : "${mapBike.number}",
		
	});
		marker.content = ${mapBike.bid};
        marker.on('click', markerClick);
        marker.emit('click', {target: marker});
	</c:forEach>
	
	function markerClick(e) {
        loadBikeInfo(e)
     }
   	 map.setFitView();
	 //加载单车信息
	function loadBikeInfo(e) {
			htmlObj = $.ajax({
				cache : true,
				type : "get",
				url : "bikeManage?requestType=20042&id="+e.target.content,
				//data : $("#pageForm").serialize(),
				async : false
			});
		var obj = jQuery.parseJSON(htmlObj.responseText);
	
		if (obj.statusCode != 200) {
			//alertMsg.error(obj.message, null);
			alert(obj.message);
		} else {
			//alert(obj.data);
			// 打开信息窗体
			infoWindow.setContent(obj.data);
      		infoWindow.open(map, e.target.getPosition());
		}

	}
</script>