<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
%>
<div class="pageContent">
	<form method="post" action="configManage?requestType=60002"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${contentVo.id}">
		<div class="pageFormContent" layoutH="58">
					<div class="unit">
							<textarea class="editor" name="content" rows="23" cols="100"
								upLinkUrl="upload.php" upLinkExt="zip,rar,txt" 
								upImgUrl="upload.php" upImgExt="jpg,jpeg,gif,png" 
								upFlashUrl="upload.php" upFlashExt="swf"
								upMediaUrl="upload.php" upMediaExt:"avi" lang="en.js">${contentVo.content}</textarea>
						</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
