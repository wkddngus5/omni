<%@page import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
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
	String funcId = "";
	if (request.getAttribute("funcId") != null) {
		funcId = request.getAttribute("funcId").toString();
	}

	String[] statisticsType = rb.getString("bike_use_statistics_type").split(",");

	String actionUrl = "bikeManage?requestType=20044&type=ajax&funcId=" + funcId;
%>
<head>
	<link href="themes/default/style.css" rel="stylesheet" type="text/css"
		  media="screen" />
	<link href="themes/css/core.css" rel="stylesheet" type="text/css"
		  media="screen" />
	<link href="themes/css/print.css" rel="stylesheet" type="text/css"
		  media="print" />
	<link href="themes/css/pikaday.css" rel="stylesheet" type="text/css" />

	<style>
		#dataTimePeriod {
			position: relative;
			width: 350px;
			margin-left: auto;
			margin-right: auto;
			height: 20px;
			z-index: 5;
			border: 1px;
			display: none;
		}
		#dataStartTime {
			display: inline;
		}
		#dataEndTime {
			display: inline;
		}
		.heatmap-panel {
			position: absolute;
			width: 130px;
			bottom: 30px;
			left: 10px;
			border: 1px;
			z-index: 5;
			visibility: hidden;
		}
		.heatmap-panel item {
			background-color: #eee;
			color: black;
			display: block;
			padding: 12px;
			cursor: pointer
		}
		.heatmap-panel item:hover {
			background-color: #ccc;
		}
	</style>
</head>

<script src="dwz/js/pikaday.min.js" type="text/javascript"></script>
<script type="text/javascript" src="chart/raphael.js"></script>
<script type="text/javascript" src="chart/g.raphael.js"></script>
<script type="text/javascript" src="chart/g.bar.js"></script>
<script type="text/javascript" src="chart/g.line.js"></script>
<script type="text/javascript" src="chart/g.pie.js"></script>
<script type="text/javascript" src="chart/g.dot.js"></script>
<script src="dwz/js/jquery-2.1.4.min.js" type="text/javascript"></script>


<body>
<div class="pageHeader">
	<form id="pageForm" onsubmit="return navTabSearch(this);"
		  class="pageForm required-validate" action="<%=actionUrl%>"
		  method="post">
		<td><input type="hidden" id="targetLocation" name="targetLocation" readonly="readonly" />
			<div class="searchBar">
				<table class="searchContent">
					<tr>
						<td><%=rb.getString("common_type_title")%>：
							<input type="radio" id="path" name="type" value="1" onclick="orbitPath();" checked="checked" /><%=statisticsType[0]%>
							<input type="radio" id="startPts"name="type" value="3" onclick="startLocation();" /><%=statisticsType[1]%>
							<input type="radio" id="endPts" name="type" value="3" onclick="endLocation();" /><%=statisticsType[2]%>
							<input type="radio" id="targetLoc" name="type" value="4" onclick="tgLocation();" /><%=statisticsType[3]%></td>
					</tr>
					<tr>
						<td><%=rb.getString("common_date_title")%>：
							<input type="text" name="startTime" value="${startTime}" class="required" id="startTime" />-
							<input type="text" name="endTime" id="endTime" value="${endTime}" class="required" /></td>
					</tr>
				</table>

				<div class="subBar">
					<ul>
						<li><div class="button">
							<div class="buttonContent">
								<button type="button" onclick="cleanPath(2)"><%=rb.getString("common_clearn_title")%></button>
							</div>
						</div></li>
						<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="button" onclick="submitForm()"><%=rb.getString("common_commit_title")%></button>
							</div>
						</div></li>
					</ul>
				</div>
			</div>
	</form>
	<div id="dataTimePeriod">
		Current time period:
		<div id ="dataStartTime"></div>
		 -
		<div id="dataEndTime"></div>
	</div>
</div>
<div id="chartHolder" style="width: 650px;height: 400px;"></div>
<div id="map" style="width:100%; height:90%"></div>
<div id="heatmapPanel" class="heatmap-panel">
	<item onclick="showHeatmap()">Heatmap Toggle</item>
	<item onclick="changeGradient()">Change Gradient</item>
	<item onclick="changeRadius()">Change Radius</item>
	<item onclick="changeOpacity()">Change Opacity</item>
</div>

<script type="text/javascript">
    var map;
    var polyPath;
    var markArr=new Array();
    var rideList = ${rideList};
    var dataType = 1;

    var heatmapLayers = 15;
    var startPoints = new Array();
    var endPoints = new Array();
    var startPointLayers = new Array(heatmapLayers);
    var endPointLayers = new Array(heatmapLayers);

    function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
            center : {
                lat : ${centerLat},
                lng : ${centerLng}
            },
            zoom : 15,
        });

        orbitPath();

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
            document.getElementById('targetLocation').value = encodeString;
        }

    }

    /* Show riding paths */
    function orbitPath() {
        dataType = 1;
        cleanPath(false);
        var triangleCoords = new Array();
        for(var i = 0;i<rideList.length;i++){
            var latLngList = rideList[i].latLngList;
            for(var j = 0;j<latLngList.length;j++){
                var location = latLngList[j];
                var myLatLng = {
                    lat : location.latitude,
                    lng : location.longitude
                };
                triangleCoords.push(myLatLng);
            }
        }
        createPolyPath(triangleCoords);
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

    /* Switch state of the markers on map between displayed and hidden*/
    function changeMarkerState(hide) {
        for(var i = 0;i<markArr.length;i++){
            markArr[i].setMap(hide ? null : map);
        }
    }

    /* Clear path */
    function cleanPath(showChart) {
        if(showChart == 1){
            document.getElementById('chartHolder').hidden = "";
            document.getElementById('map').style.height = "50%";
        }else if(showChart == 0){
            document.getElementById('chartHolder').hidden = "hidden";
            document.getElementById('map').style.height = "90%";
        }
        if(polyPath != null){
            polyPath.setMap(null);
            polyPath = null;
        }

        document.getElementById('targetLocation').value = "";
        document.getElementById("heatmapPanel").style.visibility = "hidden";
        document.getElementById("dataTimePeriod").style.display = "none";

        changeMarkerState(true);

        /* Clear start point heatmap layers */
        if(startPointLayers[0] != null) {
            for(var i = 0; i < heatmapLayers; i++) {
                startPointLayers[i].setMap(null);
            }
        }
        /* Clear end point heatmap layers */
        if(endPointLayers[0] != null) {
            for(var i = 0; i < heatmapLayers; i++) {
                endPointLayers[i].setMap(null);
            }
        }
    }

    /* Show start points in map */
    function startLocation() {
        dataType = 2;
        cleanPath(false);
        document.getElementById("heatmapPanel").style.visibility = "visible";
        for(var i = 0;i<rideList.length;i++){
            var rideVo = rideList[i];
            var myLatLng = {lat: rideVo.startLat, lng: rideVo.startLng};
            var marker = new google.maps.Marker({
                position: myLatLng,
                map: map,
                title: rideVo.startTime,
                icon:"image/map_circle.png"
            });
            markArr.push(marker);
        }
    }

    /* Show end points in map */
    function endLocation() {
        dataType = 3;
        cleanPath(false);
        document.getElementById("heatmapPanel").style.visibility = "visible";
        for(var i = 0;i<rideList.length;i++){
            var rideVo = rideList[i];
            var myLatLng = {lat: rideVo.endLat, lng: rideVo.endLng};
            var marker = new google.maps.Marker({
                position: myLatLng,
                map: map,
                title: rideVo.endTime,
                icon:"image/map_circle.png"
            });
            markArr.push(marker);
        }
    }

    /* Show heatmap */
    function showHeatmap() {
        if(document.getElementById("startPts").checked) {
            startPointHeatmap();
        }
        else if(document.getElementById("endPts").checked) {
            endPointHeatmap();
        }
    }

    /* Show start points in heatmap */
    function startPointHeatmap() {
        document.getElementById("heatmapPanel").style.visibility = "visible";
        if(startPointLayers[0] != null) {
            if(startPointLayers[0].getMap() == null) {
                changeMarkerState(true);
                document.getElementById("dataTimePeriod").style.display = "block";
                document.getElementById("dataStartTime").textContent = document.getElementById("startTime").value;
                document.getElementById("dataEndTime").textContent = document.getElementById("endTime").value;
                /* Show start point heatmap */
                for(var i = 0; i < heatmapLayers; i++) {
                    startPointLayers[i].setMap(map);
                }
            }
            else {
                /* Hide start point heatmap */
                document.getElementById("dataTimePeriod").style.display = "none";
                for(var i = 0; i < heatmapLayers; i++) {
                    startPointLayers[i].setMap(null);
                }
                changeMarkerState(false);
            }
            return;
        }
        else {
            document.getElementById("dataTimePeriod").style.display = "block";
            document.getElementById("dataStartTime").textContent = document.getElementById("startTime").value;
            document.getElementById("dataEndTime").textContent = document.getElementById("endTime").value;
            changeMarkerState(true);
            for(var i = 0; i < heatmapLayers; i++) {
                startPointLayers[i] = new google.maps.visualization.HeatmapLayer({
                    data: getStartPoints(),
                    map: map
                });
            }
        }
    }

    /* Show end points in heatmap */
    function endPointHeatmap(){
        document.getElementById("heatmapPanel").style.visibility = "visible";
        if(endPointLayers[0] != null) {
            if(endPointLayers[0].getMap() == null) {
                changeMarkerState(true);
                document.getElementById("dataTimePeriod").style.display = "block";
                document.getElementById("dataStartTime").textContent = document.getElementById("startTime").value;
                document.getElementById("dataEndTime").textContent = document.getElementById("endTime").value;
                /* Show end point heatmap */
                for(var i = 0; i < heatmapLayers; i++) {
                    endPointLayers[i].setMap(map);
                }
            }
            else {
                /* Hide end point heatmap */
                document.getElementById("dataTimePeriod").style.display = "none";
                for(var i = 0; i < heatmapLayers; i++) {
                    endPointLayers[i].setMap(null);
                }
                changeMarkerState(false);
            }
            return;
        }
        else {
            document.getElementById("dataTimePeriod").style.display = "block";
            document.getElementById("dataStartTime").textContent = document.getElementById("startTime").value;
            document.getElementById("dataEndTime").textContent = document.getElementById("endTime").value;
            changeMarkerState(true);
            for(var i = 0; i < heatmapLayers; i++) {
                endPointLayers[i] = new google.maps.visualization.HeatmapLayer({
                    data: getEndPoints(),
                    map: map
                });
            }
        }
    }

    /* Construct coordinate array using provided start point coordinate info */
    function getStartPoints() {
        if(startPoints.length > 0) return startPoints;
        for(var i = 0; i < rideList.length; i++) {
            var point = new google.maps.LatLng(rideList[i].startLat, rideList[i].startLng);
            startPoints.push(point);
        }
        return startPoints;
    }

    /* Construct coordinate array using provided end point coordinate info */
    function getEndPoints() {
        if(endPoints.length > 0) return endPoints;
        for(var i = 0; i < rideList.length; i++) {
            var point = new google.maps.LatLng(rideList[i].endLat, rideList[i].endLng);
            endPoints.push(point);
        }
        return endPoints;
    }

    /* Change gradient for heatmap visualization */
    function changeGradient() {
        var gradient = [
            'rgba(0, 255, 255, 0)',
            'rgba(0, 255, 255, 1)',
            'rgba(0, 191, 255, 1)',
            'rgba(0, 127, 255, 1)',
            'rgba(0, 63, 255, 1)',
            'rgba(0, 0, 255, 1)',
            'rgba(0, 0, 223, 1)',
            'rgba(0, 0, 191, 1)',
            'rgba(0, 0, 159, 1)',
            'rgba(0, 0, 127, 1)',
            'rgba(63, 0, 91, 1)',
            'rgba(127, 0, 63, 1)',
            'rgba(191, 0, 31, 1)',
            'rgba(255, 0, 0, 1)'
        ];
        var targetPointList = document.getElementById("startPts").checked ? startPointLayers : endPointLayers;
        if(targetPointList[0] != null) {
            for(var i = 0; i < heatmapLayers; i++) {
                targetPointList[i].set('gradient', targetPointList[i].get('gradient') ? null : gradient);
            }
        }
        else return;
    }

    /* Change heatmap display radius */
    function changeRadius() {
        var targetPointList = document.getElementById("startPts").checked ? startPointLayers : endPointLayers;
        if(targetPointList[0] != null) {
            for(var i = 0; i < heatmapLayers; i++) {
                targetPointList[i].set('radius', targetPointList[i].get('radius') ? null : 20);
            }
        }
        else return;
    }

    /* Change heatmap display opacity */
    function changeOpacity() {
        var targetPointList = document.getElementById("startPts").checked ? startPointLayers : endPointLayers;
        if(targetPointList[0] != null) {
            for(var i = 0; i < heatmapLayers; i++) {
                targetPointList[i].set('opacity', targetPointList[i].get('opacity') ? null : 0.2);
            }
        }
        else return;
    }

    /* Target location */
    function tgLocation(){
        dataType = 4;
        cleanPath(true);
    }

    var options = {
        axis : "0 0 1 1", // Where to put the labels (trbl)
        axisxstep : 16, // How many x interval labels to render (axisystep does the same for the y axis)
        shade : true, // true, false
        smooth : false, //曲线
        symbol : "circle",
        colors : [ "#F44" ]
    };

</script>
<script type="text/javascript"
		src="<%=googleApiUrl%>/maps/api/js?key=AIzaSyA5Hfwhf08LujfrRdlMiE0oQdN_8mcvSDo&callback=initMap&libraries=visualization"></script>
<script type="text/javascript">
    /* Title settings */
    title = "Statistics";
    titleXpos = 390;
    titleYpos = 85;

    var chartHolder = Raphael("chartHolder");
    $(function() {
        // Make the raphael object
        var xData = new Array();
        var yData = new Array();
        showData(xData, yData);
    });


    function showData(xData, yData) {

        chartHolder.clear();
        if (xData.length == 0) {
            xData[0] = 0;
            yData[0] = 0;
        }
        options.axisxstep = xData.length;
        var lines = chartHolder.linechart(20, // X start in pixels
            10, // Y start in pixels
            450, // Width of chart in pixels
            300, // Height of chart in pixels
            xData, // Array of x coordinates equal in length to ycoords
            yData, // Array of y coordinates equal in length to xcoords
            options // opts object
        );

        // Modify the x axis labels
        var xText = lines.axis[0].text.items;
        for ( var i in xText) { // Iterate through the array of dom elems, the current dom elem will be i
            var _oldLabel = (xText[i].attr('text') + "").split('.'), // Get the current dom elem in the loop, and split it on the decimal
                _newLabel = _oldLabel[0]; // Format the result into time strings
            xText[i].attr({
                'text' : _newLabel
            }); // Set the text of the current elem with the result
        }
        ;
    }

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

        if(obj.statusCode != 200) {
            alertMsg.error(obj.message, null);
        }
        else {
            var xData = new Array();
            var yData = new Array();
            const dataList = obj.data;
            if(dataType == 4) {
                for (var i = 0; i < dataList.length; i++) {
                    xData[i] = dataList[i].title;
                    yData[i] = dataList[i].value;
                }
                showData(xData, yData);
            }
            else {
                rideList = dataList;

                /* Clear created heatmap layers*/
                if(startPointLayers[0] != null) {
                    for(var i = 0; i < heatmapLayers; i++) {
                        startPointLayers[i].setMap(null);
                        startPointLayers[i] = null;
                    }
                }
                if(endPointLayers[0] != null) {
                    for(var i = 0; i < heatmapLayers; i++) {
                        endPointLayers[i].setMap(null);
                        endPointLayers[i] = null;
                    }
                }

                /* Clear stored point lists for heatmap */
                startPoints = [];
                endPoints = [];

                if(dataType == 1){
                    orbitPath();
                }else if(dataType == 2){
                    startLocation();
                }else if(dataType == 3){
                    endLocation();
                }
            }
        }
    }

    var startPicker = new Pikaday({
        field : document.getElementById('startTime'),
        firstDay : 1,
        minDate : new Date('2010-01-01'),
        maxDate : new Date('2020-12-31'),
        yearRange : [ 2000, 2020 ]
    });

    var endPicker = new Pikaday({
        field : document.getElementById('endTime'),
        firstDay : 1,
        minDate : new Date('2010-01-01'),
        maxDate : new Date('2020-12-31'),
        yearRange : [ 2000, 2020 ]
    });
</script>


</body>
