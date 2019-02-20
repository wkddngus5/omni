<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] violationType = rb.getString("credit_dec_score_type").split(",");//get dec score type
%>
<div class="pageContent">
	<form method="post"
		action="bikeManage?requestType=20025&uid=${useVo.uid}"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">

		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="date" value="${useVo.startTime}"/>
			<div class="unit">
				<label><%=rb.getString("bike_report_review_result_title")%>：</label>
				<input type="radio" name="type" value="1" class="required" /><%= violationType[0] %>
				<input type="radio" name="type" value="2" class="required" /><%= violationType[1] %>
				<input type="radio" name="type" value="3" class="required" /><%= violationType[2] %>
				<input type="radio" name="type" value="4" class="required" /><%= violationType[3] %>
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_use_id_title")%>：</label> <input
					type="text" name="useId" size="30" class="required" value="${useVo.id}" />
			</div>

			<div class="unit">
				<label><%=rb.getString("common_note_title")%>：</label> <input
					type="text" name="note" size="50" class="required" />
			</div>


		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
