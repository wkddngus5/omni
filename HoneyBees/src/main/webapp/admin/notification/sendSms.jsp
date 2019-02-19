<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post"
		action="notifyManage?requestType=11006&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("user_phone_title")%>：</label>
				<textarea rows="5" cols="50" name="userVo.phone" class="required"></textarea>

				<a class="btnLook" href="userManage?requestType=30002"
					lookupGroup="userVo">Select Customer</a> <a class="btnLook"
					href="adminManage?requestType=10008" lookupGroup="userVo">Select
					Admin</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
				<textarea rows="5" cols="50" name="templateVo.template"
					class="required"></textarea>
				<a class="btnLook" href="notifyManage?requestType=11002&lookup=1"
					lookupGroup="templateVo">Select</a>
			</div>

			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
