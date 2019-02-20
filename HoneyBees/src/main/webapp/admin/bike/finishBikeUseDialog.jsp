<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeTypeVo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	
%>
<div class="pageContent">

	<form method="post" action="bikeManage?requestType=20028"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${useVo.id}" />
		<div class="pageFormContent" layoutH="58">
			
			<div class="unit">
				<label><%=rb.getString("common_time_title")%> ：</label> <input
					type="number" name="duration" value="${useVo.duration}" size="30"
					class="required" />
			</div>
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
