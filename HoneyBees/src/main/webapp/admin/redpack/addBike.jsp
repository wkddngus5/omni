<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
	String id = ValueUtil.getString(request.getParameter("id"));
	String number = ValueUtil.getString(request.getParameter("number"));
%>
<div class="pageContent">

	<form method="post" action="redpackManage?requestType=70008&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("bike_number")%>：</label> <input type="text"
					name="bikeVo.number" size="30" class="required" readonly="readonly" value="<%=number%>"/>
				<a class="btnLook" href="bikeManage?requestType=20021"
					lookupGroup="bikeVo">Select</a>
			</div>
			<input type="hidden" name="bikeVo.id" class="required" value="<%=id%>"/>

			<div class="unit">
				<label><%=rb.getString("redpack_rule_title")%>：</label> <input
					type="text" name="ruleVo.id" size="30" class="required"
					readonly="readonly" /> <a class="btnLook"
					href="redpackManage?requestType=70002" lookupGroup="ruleVo">Select</a>
			</div>



			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="ruleVo.start" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>

			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="ruleVo.end" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
