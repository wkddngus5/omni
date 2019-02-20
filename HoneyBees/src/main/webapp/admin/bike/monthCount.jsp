<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    var options = {
    	stacked: false,
    	gutter:20,
		axis: "0 0 1 1", // Where to put the labels (trbl)
		axisystep: 10 // How many x interval labels to render (axisystep does the same for the y axis)
	};
	
	$(function() {
        // Creates canvas
        var r = Raphael("chartHolder");
		options.axisystep = 5;
		options.stacked=false;
		
		
     //	var data3 = [[10,20,30,50,15,25,35,50, 18, 28, 38, 24]];
        var a=new Array();
        <c:forEach items="${value}" var="item">
            a.push("${item}");
        </c:forEach>
	 //	var data3 = ["${value}"]; 
	    var data3=[];
	    data3[0]=a;
		var chart3 = r.barchart(40, 220, 620, 120, data3, options).hover(function() {
            this.flag = r.popup(this.bar.x, this.bar.y, this.bar.value).insertBefore(this);
        }, function() {
            this.flag.animate({opacity: 0}, 500, ">", function () {this.remove();});
        });
        var xa=new Array();
        <c:forEach items="${xValue}" var="xitem">
            xa.push("${xitem}");
        </c:forEach>
        var xdata =[];
        xdata[0]=xa;
//        var xdata =[["A1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",  "D10", "D11", "D12"]];
//		chart3.label([["D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",  "D10", "D11", "D12"]],true);
		chart3.label(xdata,true);
	});
</script>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="monthCount" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>
					单车编号：<input type="text"    name="bike_number" value="${bNo}"  readonly="readonly" />
				</td>
			</tr>
			<tr>
				<td>
					统计年份：<input type="text"    name="year" value="${year}"   />
				</td>
			</tr>
				<tr>
				<td>
					统计月份：<input type="text"    name="month" value="${month}"   />
				</td>
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div id="chartHolder"></div>