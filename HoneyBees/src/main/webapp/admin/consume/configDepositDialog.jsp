<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
%>
<div class="pageContent">

	<form method="post" action="consumeManage?requestType=40015"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${config.id}" />
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("consume_deposit_title")%>：</label> <input
					type="text" name="amount" size="30" value="${config.amount}"
					class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("consume_deposit_return_deal_title")%>：</label>
				<input type="text" name="return_min_day" size="3"
					value="${config.return_min_day}" class="required" /><label>-</label><input
					type="text" name="return_max_day" size="3"
					value="${config.return_max_day}" class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("consume_deposit_return_automatic_refund")%>：</label>
				<input type="radio" name="automatic_refund" value="1"
					<c:if test="${config.automatic_refund == 1 }">checked="checked"</c:if> /><%=rb.getString("common_yes_title")%>
				<input type="radio" name="automatic_refund" value="0"
					<c:if test="${config.automatic_refund == 0 }">checked="checked"</c:if> /><%=rb.getString("common_no_title")%>
			</div>
			<%@ include file="../common/page_city.jspf"%>

			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
