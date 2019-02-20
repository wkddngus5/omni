<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!--异步加载 高德地图JSAPI -->
<script
	src="//webapi.amap.com/maps?v=1.3&key=80ed3318204c5bc6744f6d33c8a59ed5&callback=init"></script>
<!--引入UI组件库异步版本main-async.js（1.0版本） -->
<script src="//webapi.amap.com/ui/1.0/main-async.js"></script>


<div id="container" style="width:100%; height:100%"></div>

<script>
	function init() {
		initAMapUI(); //这里调用initAMapUI初始化
		var map = new AMap.Map('container', {
			center : [ "${centerLng}", "${centerLat}" ],
			zoom : 11
		});
		map.plugin([ "AMap.ToolBar" ], function() {
			map.addControl(new AMap.ToolBar());
		});

		//加载PointSimplifier，loadUI的路径参数为模块名中 'ui/' 之后的部分 
		AMapUI.loadUI([ 'misc/PointSimplifier' ], function(PointSimplifier) {

			if (!PointSimplifier.supportCanvas) {
				alert('当前环境不支持 Canvas！');
				return;
			}

			var pointSimplifierIns = new PointSimplifier({
				map : map, //关联的map
				getPosition : function(dataItem) {
					//返回数据项的经纬度，AMap.LngLat实例或者经纬度数组
					return dataItem.position;
				},
				getHoverTitle : function(dataItem, idx) {
					//返回数据项的Title信息，鼠标hover时显示
					return dataItem.title;
				},
				renderOptions : {
					//点的样式
					pointStyle : {
						fillStyle : 'blue' //蓝色填充
					}
				}
			});

			var data = [];
			<c:forEach items="${mapBikes}" var="mapBike">
			data.push({
				position : [${mapBike.lng},${mapBike.lat}],
				title:${mapBike.number}
			});
			</c:forEach>
			//设置数据源，data需要是一个数组
			pointSimplifierIns.setData(data);

			//监听事件
			pointSimplifierIns.on('pointClick pointMouseover pointMouseout',
					function(e, record) {
						console.log(e.type, record);
					})
		});

	}
</script>