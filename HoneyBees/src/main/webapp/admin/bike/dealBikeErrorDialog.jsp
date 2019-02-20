<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] reviewType = rb.getString(
			"bike_report_review_result_value").split(",");//get review type
%>
<div class="pageContent">
	<form method="post"
		action="bikeManage?requestType=20018&id=${errorVo.id}&number=${errorVo.bnumber}"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">

		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("bike_report_review_result_title")%>：</label>
				<input type="radio" name="result" value="0" class="required" 
				<c:if test="${errorVo.status==0 }">checked="checked"</c:if>/><%=reviewType[1]%>
				<input type="radio" name="result" value="1" class="required" 
				<c:if test="${errorVo.status==1 }">checked="checked"</c:if>/><%=reviewType[2]%>
				<input type="radio" name="result" value="2" class="required" 
				<c:if test="${errorVo.status==2 }">checked="checked"</c:if>/><%=reviewType[3]%>
				<input type="radio" name="result" value="3" class="required" 
				<c:if test="${errorVo.status==3 }">checked="checked"</c:if>/><%=reviewType[4]%>
			</div>
			
			<div class="unit">
				<label><%=rb.getString("bike_use_id_title")%>：</label> <input
					type="text" name="useId" size="30" value="${errorVo.bike_useid}"/>
			</div>

			<div class="unit">
				<label><%=rb.getString("common_note_title")%>：</label>
				<textarea rows="5" cols="50" name="note" class="required">${errorVo.review_note}</textarea>
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_report_problem_solve")%>：</label> <input
					type="radio" name="solve" value="1" class="required" /><%=rb.getString("common_yes_title")%>
				<input type="radio" name="solve" value="0" class="required" /><%=rb.getString("common_no_title")%>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
