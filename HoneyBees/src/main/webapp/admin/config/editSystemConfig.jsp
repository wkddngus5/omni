<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="configManage?requestType=60014&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" value="${config.id}" name="id"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input type="text"
					name="name" value="${config.name}" class="required" size="50" />
			</div>
			
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
				<textarea rows="5" cols="50" name="config" class="required">${config.config}</textarea>
			</div>
			
			<div class="unit">
				<label><%=rb.getString("system_config_key")%>：</label> <input type="text"
					name="key" value="${config.key}" size="50" />
			</div>
			
			<div class="unit">
				<label><%=rb.getString("common_note_title")%>：</label>
				<textarea rows="3" cols="50" name="note">${config.note}</textarea>
			</div>

			<%@ include file="../common/page_admin_check.jspf"%>

			<div class="unit">
				<font style="color: red;"><%=rb
					.getString("notification_management_template_edit_tips")%></font>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
