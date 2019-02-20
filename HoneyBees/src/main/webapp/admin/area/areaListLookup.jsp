<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "areaManage?requestType=80001&lookup=1";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_id_title")%>：<input type="text"
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
									<button type="button" multLookup="areaVo"
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
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<c:if test="${only == false}">
					<th width="30"><input type="checkbox" class="checkboxCtrl"
						group="areaVo" /></th>
				</c:if>
				<th width="120"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<c:if test="${lookup == true}">
					<th width="80"><%=rb.getString("common_select_title")%></th>
				</c:if>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${areaList}" varStatus="status" var="area">
				<tr target="id_item" rel="${area.id}">
					<c:if test="${only == false}">
						<td><input type="checkbox" name="areaVo"
							value="{id:'${area.id}', name:'${area.name}'}" /></td>
					</c:if>
					<td>${area.id}</td>
					<td>${area.name}</td>
					<td>${area.note}</td>
					<c:if test="${lookup == true}">
						<td><a class="btnSelect"
							href="javascript:$.bringBack({id:'${area.id}', name:'${area.name}'})"
							title="Select">Select</a></td>
					</c:if>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num_dialog.jspf"%>
</div>