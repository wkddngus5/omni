<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.lang.reflect.Array"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] dateTypeTitle = rb.getString("common_statistics_date")
			.split(",");
%>

<form id="pagerForm" layoutH="99">

	<%=rb.getString("common_date_title")%>: 
	<input type="text" id="date" class="date" value="${startTime}"
		dateFmt="yyyy-MM-dd" readonly="true" class="required"/>
	<select id="dateType"
		onchange="commitSearch()">
		<%
			for (int i = 0; i < dateTypeTitle.length; i++) {
		%>
		<option value=<%=i + 1%>><%=dateTypeTitle[i]%></option>
		<%
			}
		%>
	</select> <button type="button" onclick="commitSearch()"><%=rb.getString("common_search_title")%></button>,
	<lable id="total"></lable>
	<div id="chartHolder" style="width: 650px;height: 450px;"></div>
	<div id="piecChartHolder"></div>
</form>
<script type="text/javascript">

	var $parent = navTab.getCurrentPanel();
	var currentForm = $("#pagerForm", $parent).get(0);

	var options = {
		axis : "0 0 1 1", // Where to put the labels (trbl)
		axisxstep : 16, // How many x interval labels to render (axisystep does the same for the y axis)
		shade : true, // true, false
		smooth : false, //曲线
		symbol : "circle",
		colors : [ "#F44" ]
	};
	/* Title settings */
	title = "Statistics";
	titleXpos = 390;
	titleYpos = 85;

	/* Pie Data */
	pieRadius = 130;
	pieXpos = 150;
	pieYpos = 180;

	pieLegendPos = "east";

	var totalCount = 0;
	var chartHolder = Raphael("chartHolder");
	var piecChartHolder = Raphael("piecChartHolder");
	$(function() {

		// Make the raphael object

		var xData = new Array();
		var yData = new Array();
		//totalCount = 0;
		<c:forEach items="${dataList}" var="data" varStatus="status">
		xData[${status.count-1}] = ${data.title};
		yData[${status.count-1}] = ${data.value};
		totalCount += parseInt(${data.value});
		</c:forEach>
		showData(xData, yData);

		var pieName = new Array();
		var pieData = new Array();

		<c:forEach items="${statusDataList}" var="data" varStatus="status">
		pieName[${status.count-1}] = "%%.%% – " + '${data.title}';
		pieData[${status.count-1}] = ${data.value};
		</c:forEach>
		showPieData(pieName, pieData);
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
		600, // Width of chart in pixels
		400, // Height of chart in pixels
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
		this.total.innerHTML = "<%=rb.getString("common_total_title")%>:"
				+ totalCount;
	}

	function showPieData(pieName, pieData) {

		piecChartHolder.text(titleXpos, titleYpos, title).attr({
			"font-size" : 20
		});

		piecChartHolder.clear();
		var pie = piecChartHolder.piechart(pieXpos, pieYpos, pieRadius,
				pieData, {
					legend : pieName,
					legendpos : pieLegendPos
				});
		pie.hover(function() {
			this.sector.stop();
			this.sector.scale(1.1, 1.1, this.cx, this.cy);
			if (this.label) {
				this.label[0].stop();
				this.label[0].attr({
					r : 7.5
				});
				this.label[1].attr({
					"font-weight" : 800
				});
			}
		}, function() {
			this.sector.animate({
				transform : 's1 1 ' + this.cx + ' ' + this.cy
			}, 500, "bounce");
			if (this.label) {
				this.label[0].animate({
					r : 5
				}, 500, "bounce");
				this.label[1].attr({
					"font-weight" : 400
				});
			}
		});
	}
</script>
<script type="text/javascript">
	//切换类型
	function commitSearch() {
		var url = "userManage?requestType=30004&dateType="
				+ this.dateType.value;
		url += "&date=" + currentForm.date.value;
		htmlObj = $.ajax({
			url : url,
			async : false
		});
		var obj = jQuery.parseJSON(htmlObj.responseText);

		var xData = new Array();
		var yData = new Array();
		const
		dataList = obj.data.dataList;
		totalCount = 0;
		for (var i = 0; i < dataList.length; i++) {
			xData[i] = dataList[i].title;
			yData[i] = dataList[i].value;
			totalCount += parseInt(dataList[i].value);
		}

		showData(xData, yData);
	}
</script>


