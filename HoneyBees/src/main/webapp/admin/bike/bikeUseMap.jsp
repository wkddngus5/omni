<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=80ed3318204c5bc6744f6d33c8a59ed5"></script>
<script type="text/javascript"
	src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
<!--引入UI组件库（1.0版本） -->
<script src="//webapi.amap.com/ui/1.0/main.js"></script>

<div id="container" style="width:100%; height:100%"></div>

<script>
	var map = new AMap.Map('container', {
		center : [ ${useVo.endLng}, ${useVo.endLat} ],
		zoom : 15
	});

	//起点
	var smarker = new AMap.Marker({
		map : map,
		position : [ ${useVo.startLng}, ${useVo.startLat} ],
		icon : new AMap.Icon({
			size : new AMap.Size(40, 50), //图标大小
			image : "image/bike_start_zh_cn.png",

		})
	});

	//终点
	var emarker = new AMap.Marker({
		map : map,
		position : [ ${useVo.endLng}, ${useVo.endLat} ],
		icon : new AMap.Icon({
			size : new AMap.Size(40, 50), //图标大小
			image : "image/bike_end_zh_cn.png",

		})
	});

	AMapUI.load([ 'ui/misc/PathSimplifier' ], function(PathSimplifier) {

		if (!PathSimplifier.supportCanvas) {
			alert('当前环境不支持 Canvas！');
			return;
		}

		var pathSimplifierIns = new PathSimplifier({
			zIndex : 100,
			map : map, //所属的地图实例
			getPath : function(pathData, pathIndex) {
				//返回轨迹数据中的节点信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng,lat],[lng,lat]...]
				return pathData.path;
			},
			getHoverTitle : function(pathData, pathIndex, pointIndex) {
				//返回鼠标悬停时显示的信息
				if (pointIndex >= 0) {
					//鼠标悬停在某个轨迹节点上
					return pathData.name + '，点:' + pointIndex + '/'
							+ pathData.path.length;
				}
				//鼠标悬停在节点之间的连线上
				return pathData.name + '，点数量' + pathData.path.length;
			},
			renderOptions : {
				//轨迹线的样式
				pathLineStyle : {
					strokeStyle : 'red',
					lineWidth : 6,
					dirArrowStyle : true
				}
			}
		});
		var paths = [];
		<c:forEach items="${latLngList}" var="mapLL" varStatus="status">
		if ("${mapLL.longitude}".trim() != "") {
			paths.push([ "${mapLL.longitude}", "${mapLL.latitude}" ]);
		}
		</c:forEach>

		if (paths != null) {
			//这里构建两条简单的轨迹，仅作示例
			pathSimplifierIns.setData([ {
				name : '轨迹1',
				path : paths
			} ]);
		}

	});
</script>