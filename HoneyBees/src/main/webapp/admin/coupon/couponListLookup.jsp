<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] couponType = rb.getString("coupon_type_value").split(",");//get type
	String actionUrl = "couponManage?requestType=50009";
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');"
		action="<%=actionUrl%>" method="post">
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

					<c:if test="${only == false}">
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" multLookup="couponVo"
										warn="<%=rb.getString("common_select_warn")%>">
										<%=rb.getString("common_select_title")%></button>
								</div>
							</div></li>
					</c:if>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<table class="table" width="100%" layoutH="138" targetType="dialog">
		<thead>
			<tr>
				<c:if test="${only == false}">
					<th width="30"><input type="checkbox" class="checkboxCtrl"
						group="couponVo" /></th>
				</c:if>

				<th width="80"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("consume_order_amount_title")%></th>
				<th width="120"><%=rb.getString("common_date_start_title")%></th>
				<th width="120"><%=rb.getString("common_date_end_title")%></th>
				<th width="80"><%=rb.getString("common_select_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${couponList}" varStatus="status" var="coupon">
				<tr target="id_coupon" rel="${coupon.id}">
					<c:if test="${only == false}">
						<td><input type="checkbox" name="couponVo"
							value="{id:'${coupon.id}', name:'${coupon.name}', 
						start:'${coupon.start_time}',value:'${coupon.value}', end:'${coupon.end_time}'
						, type:'${coupon.type}'}" /></td>
					</c:if>

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
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${coupon.id}', name:'${coupon.name}', 
						start:'${coupon.start_time}',value:'${coupon.value}', end:'${coupon.end_time}'
						, type:'${coupon.type}'})"
						title="Select">Select</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num_dialog.jspf"%>
</div>