<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] typeValue = rb.getString(
			"notification_management_template_type_values").split(",");
	int type = Integer
			.parseInt(request.getAttribute("type").toString());
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="notifyManage?requestType=11003&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" value="${templateVo.id}" name="id"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label> <select
					name="type">
					<%
						for (int i = 1; i < typeValue.length; i++) {
					%>
					<option value=<%=i%> <%if (type == i) {%> selected="selected" <%}%>><%=typeValue[i]%></option>
					<%
						}
					%>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_title")%>：</label> <input type="text"
					name="title" value="${templateVo.title}" size="50" />
		
			</div>
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
				<textarea rows="5" cols="50" name="content">${templateVo.template}</textarea>
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
