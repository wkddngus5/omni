<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] status = rb.getString("system_config_request_status")
			.split(",");//get status
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="configManage?requestType=60009&funcId=<%=funcId%>&id=${clientVo.ip}"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>IP：</label> <label>${clientVo.ip}</label>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_status_title")%>：</label> <input
					type="radio" name="status" value="1"
					<c:if test="${clientVo.stopClient == true}">checked="checked"</c:if> /><%=status[2]%>
				<input type="radio" name="status" value="0"
					<c:if test="${clientVo.stopClient == false}">checked="checked"</c:if> /><%=status[1]%>
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
