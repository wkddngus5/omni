<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="userManage?requestType=30009&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("user_phone_title")%>：</label> <input
					type="text" size="30"
					value="${adminVo.phone}" name="phone" />
			</div>
			<div class="unit">
				<label><%=rb.getString("login_password")%>：</label> <input
					type="password" size="30" class="required"
					value="${adminVo.password}" name="password" />
			</div>
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

