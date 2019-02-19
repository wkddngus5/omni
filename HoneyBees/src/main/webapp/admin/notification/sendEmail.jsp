<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<style>
input[type="file"] {
	color: transparent;
}
</style>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
</html>
<div class="pageContent">

	<form method="post"
		action="notifyManage?requestType=11012&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone)">

		<div class="pageFormContent" layoutH="58">
			<%@ include file="../common/page_admin_check.jspf"%>
			<div class="unit">
				<label><%=rb.getString("user_email_title")%>：</label>
				<textarea rows="5" cols="50" name="userVo.email" class="required"></textarea>
				<a class="btnLook" href="userManage?requestType=30002"
						lookupGroup="userVo">Select Customer</a>
						<a class="btnLook"
					href="adminManage?requestType=10008" lookupGroup="userVo">Select Admin</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("notification_management_email_subject")%>：</label> <input
					type="text" name="templateVo.title" size=100 value=""
					class="required" />
					<a class="btnLook" href="notifyManage?requestType=11002&lookup=1"
					lookupGroup="templateVo">Select</a>
			</div>


			<div class="unit">
				<textarea class="editor" name="templateVo.template" rows="23" cols="100"
					upLinkUrl="upload" upLinkExt="zip,rar,txt" upImgUrl="upload"
					upImgExt="jpg,jpeg,gif,png" upFlashUrl="upload" upFlashExt="swf"
					upMediaUrl="upload" upMediaExt:"avi" lang="en.js"></textarea>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

