<%@page import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeVo"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusTitle = rb.getString("user_auth_status_value")
			.split(",");
	int status = ValueUtil.getInt(request.getAttribute("status"));
	if (status > 0) {
		status++;
	}
%>
<div class="pageContent">
	<form method="post"
		action="userManage?requestType=30005&id=${userVo.uId}"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("user_phone_title")%>：</label> <input
					type="text" size="30" readonly="readonly" value="${userVo.phone}" />
			</div>
			<div class="unit">
				<label><%=rb.getString("user_auth_status_title")%>：</label> <select
					name="status">
					<%
						for (int i = 1; i < statusTitle.length; i++) {
					%>
					<option value="<%=i - 1%>" <%if (i == status) {%> selected="selected"
						<%}%>><%=statusTitle[i]%></option>
					<%
						}
					%>
				</select>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
