<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post"
		action="messageManage?requestType=90002"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("user_title")%>：</label> <input type="text"
					name="userVo.name" size="30" class="required" readonly="readonly" />
				<a class="btnLook" href="userManage?requestType=30002"
					lookupGroup="userVo">Select</a>
			</div>
			<input type="hidden" name="userVo.id" size="30" class="required"
				readonly="readonly" />


			<div class="unit">
				<label><%=rb.getString("common_title")%>：</label> <input type="text"
					name="title" size="50" class="required" />

			</div>
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
					<textarea rows="4" cols="45" name="content" class="required" placeholder="<%=rb.getString("common_input_tips_title")%>"></textarea>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
