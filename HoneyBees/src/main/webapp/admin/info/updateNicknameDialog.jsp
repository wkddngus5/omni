<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
%>
<div class="pageContent">
	<form method="post" action="adminManage?requestType=10002"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("nickname_title") %>：</label> <input
					type="text" name="nickname" size="30" class="required" />
			</div>
			
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

