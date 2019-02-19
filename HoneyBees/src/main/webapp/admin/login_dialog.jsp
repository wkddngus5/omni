<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
%>
<div class="pageContent">

	<form method="post" action="login" class="pageForm"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("login_username")%>:</label> <input
					type="text" name="username" size="30" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("login_password")%>:</label> <input
					type="password" name="password" size="30" class="required" />
			</div>
		</div>
		<%@ include file="common/page_commit.jspf"%>
	</form>

</div>

