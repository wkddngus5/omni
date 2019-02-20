<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="com.pgt.bikelock.utils.LanguageUtil" %>
<%@ page language="java" import="com.pgt.bikelock.vo.WebContentVo" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contentType[] = LanguageUtil.getDefaultValue("setting_webcontent_type_value").split(",");
WebContentVo agreement= (WebContentVo) request.getAttribute("agreementVo");
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>
    <c:if test="${agreementVo == null}">agreement not exist</c:if>
    <c:if test="${agreementVo != null}"> <%= contentType[agreement.getType()-1] %></c:if>
   </title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <c:if test="${agreementVo == null}">agreement not exist</c:if>
    <c:if test="${agreementVo != null}"> ${agreementVo.content}</c:if>
  </body>
</html>
