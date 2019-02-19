<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] couponType = rb.getString("coupon_type_value").split(",");//get type
	String actionUrl = "couponManage?requestType=50001";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_name_title")%>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>

				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("consume_order_amount_title")%></th>
				<th width="120"><%=rb.getString("common_date_start_title")%></th>
				<th width="120"><%=rb.getString("common_date_end_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${couponList}" varStatus="status" var="coupon">
				<tr target="id_item" rel="${coupon.id}">
					<td>${coupon.id}</td>
					<td>${coupon.name}</td>
					<td><c:if test="${coupon.type==1}">
							<%=couponType[0]%>
						</c:if> <c:if test="${coupon.type==2}">
							<%=couponType[1]%>
						</c:if> <c:if test="${coupon.type==3}">
							<%=couponType[2]%>
						</c:if> <c:if test="${coupon.type==4}">
							<%=couponType[3]%>
						</c:if></td>

					<td>${coupon.value}</td>
					<td>${coupon.start_time}</td>
					<td>${coupon.end_time}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>