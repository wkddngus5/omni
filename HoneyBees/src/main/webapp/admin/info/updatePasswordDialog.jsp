<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
%>
<div class="pageContent">
	<form method="post" action="adminManage?requestType=10001"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("current_password_title") %>：</label> <input
					type="password" name="oldPassword" size="30" class="required"/>
			</div>

			<div class="unit">
				<label><%=rb.getString("new_password_title") %>：</label> <input type="password" name="newPassword" size="30"
					class="required" />
			</div>

			<div class="unit">
				<label><%=rb.getString("confirm_new_password_title") %>：</label> <input type="password" name="confirmPassword" size="30"
					class="required" />
			</div>
			
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

