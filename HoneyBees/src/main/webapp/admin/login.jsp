<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="com.pgt.bikelock.resource.OthersSource" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String lang = (String) session.getAttribute("lang");
	String pageTitle = rb.getString("page_title").replace("%s", OthersSource.getSourceString("project_name"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="keywords" content="<%=pageTitle%>">
<meta http-equiv="description" content="<%=pageTitle%>">
<link rel="icon" href="tip.ico" type="image/x-icon" />
<title><%=pageTitle%></title>
<link href="themes/css/login.css" rel="stylesheet" type="text/css" />
<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
</head>

<body>
	<div id="login">
		<div id="login_header">
			<h1 class=login_left_text>
				<%=pageTitle%>
			</h1>

			<div class="login_headerContent">
				<div class="navList">
					<%
						if ("ru_mo".equals(lang)) {
					%>
					<%=rb.getString("login_tip_title")%>
					<%
						}
					%>
				</div>
				<h2 class="login_rigth_text">

					<%
						if (!"ru_mo".equals(lang)) {
					%>
					<%=rb.getString("login_tip_title")%>
					<%
						}
					%>
				</h2>

			</div>

		</div>
		<div id="login_content">
			<div class="loginForm">
				<form action="login" method="post">
					<p>
						<label><%=rb.getString("login_username")%>：</label> <input
							type="text" name="username" size="20" class="login_input"
							 />
					</p>
					<p>
						<label><%=rb.getString("login_password")%>：</label> <input
							type="password" name="password" size="20" class="login_input" />
					</p>
					
					<div style="color: red;margin: 10px;">
					${error_tips}
					</div>
					
					<div class="login_bar">
						<input class="sub" type="submit"
							value="<%=rb.getString("login")%>" />
					</div>
					
				</form>
			</div>
			<div class="login_banner">
				<img src="themes/default/images/login_banner.png" />
			</div>
			<div class="login_main">
				<ul class="helpList">

				</ul>

			</div>
		</div>
		<div id="login_footer">Copyright &copy; 2016 Inc. All Rights
			Reserved.</div>
	</div>
</body>
</html>
