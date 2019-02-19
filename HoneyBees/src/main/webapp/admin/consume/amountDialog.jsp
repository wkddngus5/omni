<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post"
		action="consumeManage?requestType=<c:if test="${amount.id==null}">40013</c:if>
	<c:if test="${amount.id!=null}">40012</c:if>&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${amount.id}" />
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("consume_recharge_amount_title")%>：</label> <input
					type="text" name="amount" size="30" value="${amount.amount}"
					class="required" />
			</div>
			<div class="unit">
				<label><%=rb.getString("consume_gift_amount_title")%>：</label> <input
					type="text" name="gift" size="30" value="${amount.gift}"
					class="required" />
			</div>
			<%@ include file="../common/page_city.jspf"%>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
