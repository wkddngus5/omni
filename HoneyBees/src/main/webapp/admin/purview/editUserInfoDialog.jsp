<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="adminManage?requestType=10004&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("nickname_title")%>：</label> <input
					type="text" size="30" class="required" value="${adminVo.nickname}"
					name="nickName" />
			</div>
			<div class="unit">
				<label><%=rb.getString("login_username")%>：</label> <input
					type="text" size="30" class="required" value="${adminVo.username}"
					name="userName" />
			</div>
			<div class="unit">
				<label><%=rb.getString("login_password")%>：</label> <input
					type="password" size="30" class="required"
					value="${adminVo.password}" name="password" />
			</div>
			<div class="unit">
				<label><%=rb.getString("user_phone_title")%>：</label> <input
					type="text" size="30"
					value="${adminVo.phone}" name="phone" />
			</div>
			<div class="unit">
				<label><%=rb.getString("user_email_title")%>：</label> <input
					type="text" size="30" 
					value="${adminVo.email}" name="email" />
			</div>
			
			<div class="unit">
				<label><%=rb.getString("system_config_manager_admin")%>：</label>
				<input type="radio" name="isAdmin" value="1"
					<c:if test="${adminVo.isAdmin == 1 }">checked="checked"</c:if> /><%=rb.getString("common_yes_title")%>
				<input type="radio" name="isAdmin" value="0"
					<c:if test="${adminVo.isAdmin == 0 }">checked="checked"</c:if> /><%=rb.getString("common_no_title")%>
			</div>

			<c:if test="${adminVo != null}">
				<input type="hidden" name="id" value="${adminVo.id}" />
			</c:if>

			<%@ include file="../common/page_city.jspf"%>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
