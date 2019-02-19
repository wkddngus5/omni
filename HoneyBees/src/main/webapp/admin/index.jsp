<%@page import="javax.print.attribute.standard.MediaSize.Other"%>
<%@page import="com.pgt.bikelock.resource.OthersSource"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] langType = OthersSource.getSourceString("language_option")
			.split(",");//get language type
	int currentLang = ValueUtil.getInt(session
			.getAttribute("currentLang"));
	String title = rb.getString("page_title").replace("%s",
			OthersSource.getSourceString("project_name"));
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title><%=title%></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="<%=title%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="themes/default/style.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/core.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/print.css" rel="stylesheet" type="text/css"
	media="print" />
<link href="uploadify/css/uploadify.css" rel="stylesheet"
	type="text/css" media="screen" />

<link rel="icon" href="tip.ico" type="image/x-icon" />
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<!--[if IE]>
<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lt IE 9]><script src="js/speedup.js" type="text/javascript"></script><script src="dwz/js/jquery-1.11.3.min.js" type="text/javascript"></script><![endif]-->
<!--[if gte IE 9]><!-->
<script src="dwz/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<!--<![endif]-->

<script src="dwz/js/jquery.cookie.js" type="text/javascript"></script>
<script src="dwz/js/jquery.validate.js" type="text/javascript"></script>
<script src="dwz/js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="xheditor/xheditor-1.2.2.min.js" type="text/javascript"></script>
<script src="xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="uploadify/scripts/jquery.uploadify.js"
	type="text/javascript"></script>

<!-- svgå¾è¡¨  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->
<script type="text/javascript" src="chart/raphael.js"></script>
<script type="text/javascript" src="chart/g.raphael.js"></script>
<script type="text/javascript" src="chart/g.bar.js"></script>
<script type="text/javascript" src="chart/g.line.js"></script>
<script type="text/javascript" src="chart/g.pie.js"></script>
<script type="text/javascript" src="chart/g.dot.js"></script>



<script src="dwz/js/dwz.core.js" type="text/javascript"></script>
<script src="dwz/js/dwz.util.date.js" type="text/javascript"></script>
<script src="dwz/js/dwz.validate.method.js" type="text/javascript"></script>
<script src="dwz/js/dwz.barDrag.js" type="text/javascript"></script>
<script src="dwz/js/dwz.drag.js" type="text/javascript"></script>
<script src="dwz/js/dwz.tree.js" type="text/javascript"></script>
<script src="dwz/js/dwz.accordion.js" type="text/javascript"></script>
<script src="dwz/js/dwz.ui.js" type="text/javascript"></script>
<script src="dwz/js/dwz.theme.js" type="text/javascript"></script>
<script src="dwz/js/dwz.switchEnv.js" type="text/javascript"></script>
<script src="dwz/js/dwz.alertMsg.js" type="text/javascript"></script>
<script src="dwz/js/dwz.contextmenu.js" type="text/javascript"></script>
<script src="dwz/js/dwz.navTab.js" type="text/javascript"></script>
<script src="dwz/js/dwz.tab.js" type="text/javascript"></script>
<script src="dwz/js/dwz.resize.js" type="text/javascript"></script>
<script src="dwz/js/dwz.dialog.js" type="text/javascript"></script>
<script src="dwz/js/dwz.dialogDrag.js" type="text/javascript"></script>
<script src="dwz/js/dwz.sortDrag.js" type="text/javascript"></script>
<script src="dwz/js/dwz.cssTable.js" type="text/javascript"></script>
<script src="dwz/js/dwz.stable.js" type="text/javascript"></script>
<script src="dwz/js/dwz.taskBar.js" type="text/javascript"></script>
<script src="dwz/js/dwz.ajax.js" type="text/javascript"></script>
<script src="dwz/js/dwz.pagination.js" type="text/javascript"></script>
<script src="dwz/js/dwz.database.js" type="text/javascript"></script>
<script src="dwz/js/dwz.datepicker.js" type="text/javascript"></script>
<script src="dwz/js/dwz.effects.js" type="text/javascript"></script>
<script src="dwz/js/dwz.panel.js" type="text/javascript"></script>
<script src="dwz/js/dwz.checkbox.js" type="text/javascript"></script>
<script src="dwz/js/dwz.history.js" type="text/javascript"></script>
<script src="dwz/js/dwz.combox.js" type="text/javascript"></script>
<script src="dwz/js/dwz.print.js" type="text/javascript"></script>
<!-- å¯ä»¥ç¨dwz.min.jsæ¿æ¢åé¢å¨é¨dwz.*.js (æ³¨æï¼æ¿æ¢æ¶ä¸é¢dwz.regional.zh.jsè¿éè¦å¼å¥)
<script src="bin/dwz.min.js" type="text/javascript"></script>
-->

<%
	if (currentLang == 1) {
%>
<script src="dwz/js/dwz.regional.zh.js" type="text/javascript"></script>
<%
	} else {
%>
<script src="dwz/js/dwz.regional.en.js" type="text/javascript"></script>
<%
	}
%>


<script type="text/javascript">
	


	$(function() {
		DWZ.init("dwz.frag.jsp", {
			loginUrl : "login_dialog.jsp",
			loginTitle : "login", // å¼¹åºç»å½å¯¹è¯æ¡
			//		loginUrl:"login.html",	// è·³å°ç»å½é¡µé¢
			statusCode : {
				ok : 200,
				error : 300,
				timeout : 301
			}, //ãå¯éã
			pageInfo : {
				pageNum : "pageNum",
				numPerPage : "numPerPage",
				orderField : "orderField",
				orderDirection : "orderDirection"
			}, //ãå¯éã
			keys : {
				statusCode : "statusCode",
				message : "message"
			}, //ãå¯éã
			ui : {
				hideMode : 'offsets'
			}, //ãå¯éãhideMode:navTabç»ä»¶åæ¢çéèæ¹å¼ï¼æ¯æçå¼æâdisplayâï¼âoffsetsâè´æ°åç§»ä½ç½®çå¼ï¼é»è®¤å¼ä¸ºâdisplayâ
			debug : false, // è°è¯æ¨¡å¼ ãtrue|falseã
			callback : function() {
				initEnv();
				$("#themeList").theme({
					themeBase : "themes"
				}); // themeBase ç¸å¯¹äºindexé¡µé¢çä¸»é¢baseè·¯å¾
			}
		});
	});
</script>

</head>

<body>
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a style="color: white;font-size: 20px; margin-top: 10px;"><%=title%></a>

				<ul class="nav">

					<li><label id="date" style="color: white;"></label></li>

					<li><label style="color: white;"><%=rb.getString("language_title")%>:</label>
						<select name="currentLang" onchange="changeLanguage(this)">
							<%
								for (int i = 0; i < langType.length; i++) {
							%>
							<option value="<%=i + 1%>" <%if (i + 1 == currentLang) {%>
								selected="selected" <%}%>><%=langType[i]%>
							</option>
							<%
								}
							%>
					</select></li>
					<c:if test="${admin == 1}">
						<li><label style="color: white;"><%=rb.getString("user_area_title")%>:</label>
							<select name="cityId" onchange="changeCity(this)">
								<option value="0"><%=rb.getString("common_all_title")%></option>
								<c:forEach items="${cityList}" varStatus="status" var="city">
									<option value="${city.id}"
										<c:if test="${city.id == cityId}">selected="selected"</c:if>><td>${city.name}</td>
									</option>
								</c:forEach>

						</select></li>
					</c:if>

					<li><a href="login.jsp"><%=rb.getString("login_out")%></a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">èè²</div></li>
					<li theme="green"><div>ç»¿è²</div></li>
					<!--<li theme="red"><div>çº¢è²</div></li>-->
					<li theme="purple"><div>ç´«è²</div></li>
					<li theme="silver"><div>é¶è²</div></li>
					<li theme="azure"><div>å¤©è</div></li>
				</ul>
			</div>

			<!-- navMenu -->

		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse">
						<div></div>
					</div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse">
					<h2>Menu</h2>
					<div>æ¶ç¼©</div>
				</div>
				<div class="accordion" fillSpace="sidebar">
					<c:forEach items="${functionList}" varStatus="parent"
						var="function">
						<div class="accordionHeader">
							<h2>
								<span>Folder</span>${function.name}</h2>
						</div>
						<div class="accordionContent">
							<ul class="tree treeFolder">
								<c:forEach items="${function.subList}" varStatus="sub"
									var="subFunction">
	
									
										<li><a href="${subFunction.href}&funcId=${subFunction.id}"
											target="navTab"
											<c:if test="${subFunction.external==1}">external="true"</c:if>
											rel="${subFunction.ref}">${subFunction.name}</a></li>
	
									
	
								</c:forEach>
							</ul>
						</div>
					</c:forEach>
				</div>


			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- æ¾ç¤ºå·¦å³æ§å¶æ¶æ·»å  class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span
										class="home_icon"><%=rb.getString("my_homepage")%></span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div>
					<!-- ç¦ç¨åªéè¦æ·»å ä¸ä¸ªæ ·å¼ class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div>
					<!-- ç¦ç¨åªéè¦æ·»å ä¸ä¸ªæ ·å¼ class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;"><%=rb.getString("my_homepage")%></a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
							<p>
								<span> <a href="info/updateNicknameDialog.jsp"
									target="dialog" mask="true"
									title="<%=rb.getString("update_nickname_title")%>"><%=session.getAttribute("adminNickName")%></a>,
									<%=rb.getString("welcome_content").replace("%s",
					OthersSource.getSourceString("project_name"))%> ,<a
									href="info/updatePasswordDialog.jsp" target="dialog"
									mask="true"> <%=rb.getString("update_password_title")%></a>
								</span>
							</p>
						</div>
						<div class="pageFormContent" layoutH="80">
							<!-- 
						        <img src="home.jpg" alt="é¦é¡µ" style="width: 100%;height: 100%" /> 
						     -->

						</div>

					</div>

				</div>
			</div>
		</div>

	</div>

	<%-- <form action="${jumpUrl}" id="jumpForm"> --%>
	</form>
</body>
<script type="text/javascript" src="dwz/js/dwz.dialog.js"></script>
<script type="text/javascript">

	<c:if test="${jumpUrl != null}">
		navTab.init(null);
		navTab.openTab("test","${jumpUrl}","");
	</c:if>
	//åæ¢è¯­è¨
	function changeLanguage(tag) {
		var lang;
		const
		value = tag.value;
		if (value == 1) {
			lang = "zh_cn";
		} else if (value == 2) {
			lang = "en_us";
		} else if (value == 3) {
			lang = "ru_mo";
		}
		window.location.href = "adminManage?requestType=10007&lang=" + lang
				+ "&currentLang=" + value;
	}
	//åæ¢åå¸
	function changeCity(tag) {
		window.location.href = "areaManage?requestType=80005&cityId="
				+ tag.value;
	}

	function getCurDate() {
		var d = new Date();
		/* var week;
		switch (d.getDay()) {
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		default:
			week = "星期天";
		} */
		var years = d.getFullYear();
		var month = add_zero(d.getMonth() + 1);
		var days = add_zero(d.getDate());
		var hours = add_zero(d.getHours());
		var minutes = add_zero(d.getMinutes());
		var seconds = add_zero(d.getSeconds());
		var ndate = years + "-" + month + "-" + days + " " + hours + ":"
				+ minutes + ":" + seconds;
		var date = document.getElementById("date");
		date.innerHTML = ndate;
	}
	function add_zero(temp) {
		if (temp < 10)
			return "0" + temp;
		else
			return temp;
	}
	setInterval("getCurDate()", 100);

</script>

</html>

