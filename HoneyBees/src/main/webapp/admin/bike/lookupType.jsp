<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
%>
<form id="pagerForm" action="toBikeTypeLookup">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" /> 
</form>
<div class="pageContent">
	<table class="table" layoutH="50" targetType="dialog" width="100%">
		<thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title") %></th>
				<th width="120"><%=rb.getString("bike_type_unit") %></th>
				<th width="120"><%=rb.getString("bike_type_count") %></th>
				<th width="120"><%=rb.getString("bike_type_price") %></th>
				<th width="80"><%=rb.getString("common_select_title") %></th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${list}" var="bikeType" varStatus="status">
		        <tr>
					<td>${status.count}</td>
					<td>${bikeType.name}</td>
					<td>${bikeType.price}</td>
					<td>
						<a class="btnSelect" href="javascript:$.bringBack({id:'${bikeType.id}', name:'${bikeType.name}'})" 
						title="<%=rb.getString("common_select_title") %>"><%=rb.getString("common_select_title") %></a>
					</td>
		    	</tr>
		    </c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>每页</span>

			<select name="numPerPage" onchange="dwzPageBreak2({targetType:'dialog', numPerPage:this.value})">
				<c:if test="${numPerPage==10}">
				    <option value="10" selected="selected">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
			    </c:if>
			     <c:if test="${numPerPage==15}">
				    <option value="10" >10</option>
					<option value="15" selected="selected">15</option>
					<option value="20">20</option>
			    </c:if>
				<c:if test="${numPerPage==20}">
				    <option value="10" >10</option>
					<option value="15" >15</option>
					<option value="20" selected="selected">20</option>
			    </c:if>
			</select>
			<span>条，共${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialog" totalCount="${totalCount}" numPerPage="${numPerPage}" pageNumShown="6" currentPage="${pageNum}"></div>
	</div>
</div>