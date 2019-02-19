<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=80ed3318204c5bc6744f6d33c8a59ed5"></script>
<script type="text/javascript"
	src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
<div id="container" style="width:100%; height:100%"></div>

<script>
	var map = new AMap.Map('container', {
		resizeEnable : true,
		zoom:18,
		center : [ "${lng}", "${lat}" ]
	});
	var infoWindow = new AMap.InfoWindow({
		offset : new AMap.Pixel(0, -30)
	});


	var marker = new AMap.Marker({
		map : map,
		position : [ "${lng}", "${lat}" ],
/* 		icon : new AMap.Icon({
			size : new AMap.Size(40, 50), //图标大小
			image : "${mapBike.icon}",
			
		}), */
		title : "${title}",
	});

</script>