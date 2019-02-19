<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] payType = rb.getString("user_cash_money_pay_way_value")
			.split(",");//get pay type
%>
<div class="pageContent">
	<form method="post" action="userManage?requestType=30003"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			
			<input type="hidden" name="userId" value="${userVo.uId}">
		
			<div class="unit">
				<label><%=rb.getString("consume_order_amount_title")%>：</label> <input
					type="text" name="amount" size="30" class="required"
					value="${userVo.money}" />
			</div>

			<div class="unit">
				<label><%=rb.getString("consume_order_pay_way_title")%>：</label> <select
					name="type">
					<%
						for (int i = 0; i < payType.length; i++) {
							String typeStr = payType[i];
					%>
					<option value="<%=i%>"><%=typeStr%></option>
					<%
						}
					%>
				</select>


			</div>

			<div class="unit">
				<label><%=rb.getString("consume_id_title")%>：</label> <input
					type="text" name="orderId" size="30" class="required" />
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
