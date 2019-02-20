<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.lang.reflect.Array"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] dataTypeTitle = rb.getString(
			"bike_order_record_type_value").split(",");
	String[] dateTypeTitle = rb.getString("common_statistics_date")
			.split(",");
%>


<%=rb.getString("common_type_title")%>:
<select id="dataType" onchange="changeType()">
	<%
		for (int i = 0; i < dataTypeTitle.length; i++) {
	%>
	<option value=<%=i%>><%=dataTypeTitle[i]%></option>
	<%
		}
	%>
</select>
,


<%=rb.getString("common_date_title")%>:
<select id="dateType" onchange="changeType()">
	<%
		for (int i = 0; i < dateTypeTitle.length; i++) {
	%>
	<option value=<%=i + 1%>><%=dateTypeTitle[i]%></option>
	<%
		}
	%>
</select>
,
<lable id="total"></lable>
<div id="chartHolder" style="width: 650px;height: 450px;"></div>
<script type="text/javascript">
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


	var totalCount = 0;
	var chartHolder = Raphael("chartHolder");
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
		this.total.innerHTML = "<%=rb.getString("common_total_title")%>:" + totalCount;
	}

</script>
<script type="text/javascript">
	//切换类型
	function changeType() {
		var url = "bikeManage?requestType=20030&id=${id}&dateType="
				+ this.dateType.value;
		url += "&dataType=" + this.dataType.value;

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


