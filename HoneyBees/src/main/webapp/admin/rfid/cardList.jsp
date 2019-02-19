<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] status = rb.getString("rfid_card_status_value").split(",");//get status
	int cardStatus = ValueUtil.getInt(request.getAttribute("status"));
	String actionUrl = "rfidManage?requestType=101";
	boolean export = true;
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
					<td>
					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < status.length; i++) {
							%>
							<option value=<%=i%> <%if (cardStatus == i) {%>
								selected="selected" <%}%>><%=status[i]%></option>
							<%
								}
							%>
					</select></td>
					<td><%=rb.getString("common_date_title")%>： <input
						type="text" name="startTime" class="date" value="${startTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> -
						<input type="text" name="endTime" class="date" value="${endTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /></td>

					
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
				<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<%-- <th width="100"><%=rb.getString("common_id_title")%></th> --%>
				<th width="120"><%=rb.getString("rfid_card_id")%></th>
				<th width="120"><%=rb.getString("rfid_card_no")%></th>
				<th width="120"><%=rb.getString("user_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cardList}" varStatus="status" var="card">
				<tr target="id_item" rel="${card.id}">
				<td><input name="ids" value="'${card.id}'" type="checkbox"></td>
					<%-- <td>${card.id}</td> --%>
					<td>${card.cardId}</td>
					<td>${card.cardNo}</td>
					<td><c:if test="${card.userVo != null}"><a href="userManage?requestType=30006&id=${card.uid}" target="navTab">${card.userVo.phone}</a></c:if></td>
					<td><c:if test="${card.status==0}">
							<%=status[1]%>
						</c:if> <c:if test="${card.status==1}">
							<%=status[2]%>
						</c:if> <c:if test="${card.status==2}">
							<%=status[3]%></c:if>
						</td>
					<td><fmt:formatDate value="${card.date}" pattern="yyyy-MM-dd HH:mm"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>