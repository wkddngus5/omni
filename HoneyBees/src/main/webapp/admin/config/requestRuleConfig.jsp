<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="configManage?requestType=60008&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("system_config_request_setting_interval")%>：</label>
				<input type="number" name="REQUEST_INTERVAL"
					value="${REQUEST_INTERVAL}" size="30" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("system_config_request_setting_total_count")%>：</label>
				<input type="number" name="REQUEST_MAX_REQUEST_COUNT"
					value="${REQUEST_MAX_REQUEST_COUNT}" size="30" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("system_config_request_setting_web_count")%>：</label>
				<input type="number" name="OTHERS_REQUEST_MAX_REQUEST_COUNT"
					value="${OTHERS_REQUEST_MAX_REQUEST_COUNT}" size="30" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("system_config_request_setting_sms_interval")%>：</label>
				<input type="number" name="SMS_REQUEST_INTERVAL"
					value="${SMS_REQUEST_INTERVAL}" size="30" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("system_config_request_setting_sms_count")%>：</label>
				<input type="number" name="SMS_COUNT_MAX_IN_DAY"
					value="${SMS_COUNT_MAX_IN_DAY}" size="30" class="required" />
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
