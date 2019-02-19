<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeVo"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] payStatusTitle = rb.getString(
			"consume_order_pay_status_value").split(",");//get pay status type
%>
<div class="pageContent">
	<form method="post" action="consumeManage?requestType=40017"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="id" value="${tradeVo.id}" />

			<div class="unit">
				<label><%=rb.getString("consume_order_amount_title")%>：</label> <input
					type="text" name="amount" size="30" value="${tradeVo.amount}"  class="required"/>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_status_title")%>：</label> <input
					type="radio" name="status" value="0" class="required"
					<c:if test="${tradeVo.status==0 }">checked="checked"</c:if> /><%=payStatusTitle[1]%>
				<input type="radio" name="status" value="1" class="required"
					<c:if test="${tradeVo.status==1 }">checked="checked"</c:if> /><%=payStatusTitle[2]%>
			</div>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
