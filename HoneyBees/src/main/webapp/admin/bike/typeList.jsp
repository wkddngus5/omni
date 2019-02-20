<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));

String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
pageContext.setAttribute("unitType", unitType);

String actionUrl = "bikeManage?requestType=20015";
boolean export = false;
%>
<div class="pageHeader" >
</div>
<div class="pageContent">
   <%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
	    <thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title") %></th>
				<th width="120"><%=rb.getString("bike_type_unit") %></th>
				<th width="120"><%=rb.getString("bike_type_count") %></th>
				<th width="120"><%=rb.getString("bike_type_price") %></th>
				<th width="120">Hold <%=rb.getString("bike_type_unit") %></th>
				<th width="120">Hold <%=rb.getString("bike_type_count") %></th>
				<th width="120">Hold <%=rb.getString("bike_type_price") %></th>
				<th width="120">Hold Max <%=rb.getString("bike_type_count") %></th>
				
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${typeList}" varStatus="status" var="type">
		        <tr target="id_item" rel="${type.id}">
		            <td>${type.id}</td>
		            
		            <td>
		            
		            <c:if test="${type.unit_type == 1}">
		            <%= unitType[0] %>
		            </c:if>
		             <c:if test="${type.unit_type == 2}">
		            <%= unitType[1] %>
		            </c:if>
		             <c:if test="${type.unit_type == 3}">
		             <%= unitType[2] %>
		            </c:if>
		            </td>
		            <td>${type.count}</td>
		            <td>${type.price}</td>

		            <td>${unitType[type.holdUnitType - 1]}</td>
		            <td>${type.holdCount}</td>
		            <td>${type.holdPrice}</td>
		            <td>${type.holdMaxCount}</td>

		           
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
	<div class="panelBar"></div>

</div>