<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String actionUrl = "adminManage?requestType=10003";

%>

<div class="pageContent">

	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
			<c:if test="${only == false}">
					<th width="30"><input type="checkbox" class="checkboxCtrl"
						group="adminVo" /></th>
				</c:if>
				<th width="80"><%= rb.getString("common_id_title") %></th>
				<th width="120"><%= rb.getString("login_username") %></th>
				<th width="120"><%= rb.getString("nickname_title") %></th>
				<th width="120"><%= rb.getString("user_phone_title") %></th>
				<th width="120"><%= rb.getString("user_email_title") %></th>
				<th width="120"><%=rb.getString("common_select_title") %></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${adminList}" varStatus="status" var="admin">
				<!-- target 在删除和修改的时候用到，选择项的ID -->
				<tr target="id_item" rel="${admin.id}">
				<c:if test="${only == false}">
						<td><input type="checkbox" name="adminVo"
							value="{id:'${admin.id}', name:'${admin.nickname}'
							, phone:'${admin.phone}', email:'${admin.email}'}" /></td>
					</c:if>
					<td>${admin.id}</td>
					<td>${admin.username}</td>
					<td>${admin.nickname}</td>
					<td>${admin.phone}</td>
					<td>${admin.email}</td>
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${admin.id}', name:'${admin.nickname}'
						, phone:'${admin.phone}', email:'${admin.email}'})"
						title="<%=rb.getString("common_select_title") %>"><%=rb.getString("common_select_title") %></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>