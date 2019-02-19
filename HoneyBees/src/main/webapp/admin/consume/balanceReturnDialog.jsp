<%@page import="com.pgt.bikelock.utils.ValueUtil"%>
<%@page import="com.pgt.bikelock.resource.OthersSource"%>
<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
%>

<div class="pageFormContent" layoutH="60">
	<fieldset>
		<legend><%=rb.getString("consume_balance_return_base_info")%></legend>


		<dl>
			<dt><%=rb.getString("user_id_title")%>：
			</dt>
			<dd>${cashVo.uid}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("user_phone_title")%>：
			</dt>
			<dd>${cashVo.userVo.phone}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("consume_order_amount_title")%>：
			</dt>
			<dd>${cashVo.amount}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("consume_balance_refund_amount")%>：
			</dt>
			<dd>
				<input type="number" id="amount" value="${cashVo.refund_amount}"/></dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_status_title")%>：
			</dt>
			<dd>${cashVo.statusStr}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("common_date_title")%>：
			</dt>
			<dd>${cashVo.date}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_deal_data_title")%>：
			</dt>
			<dd>${cashVo.refund_date}</dd>
		</dl>


	</fieldset>


	<fieldset>
		<legend><%=rb.getString("consume_balance_return_recharge_list")%></legend>
		<div class="panelBar">

			<ul class="toolBar">
				<li><a class="edit" onclick="refund()"
					title="<%=rb.getString("consume_order_refund_confirm")%>"> <span><%=rb.getString("consume_order_refund_title")%></span>
				</a></li>
			</ul>
		</div>
		<table class="table" width="100%" layoutH="138">
			<thead>
				<tr>
					<th width="80" orderField="id"><%=rb.getString("common_id_title")%></th>
					<th width="120"><%=rb.getString("consume_order_amount_title")%></th>
					<th width="120"><%=rb.getString("user_money_title")%></th>
					<th width="120"><%=rb.getString("consume_order_pay_way_title")%></th>
					<th width="120"><%=rb.getString("common_status_title")%></th>
					<th width="120"><%=rb.getString("consume_order_pay_out_id")%></th>
					<th width="120"><%=rb.getString("common_date_title")%></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tradeList}" varStatus="status" var="consume">
					<tr target="id_item" rel="${consume.id}">
						<td>${consume.id}</td>
						<td>${consume.amount}</td>
						<td>${consume.balance}</td>
						<td>${consume.wayStr}</td>
						<td>${consume.statusStr}</td>
						<td>${consume.out_trade_no}</td>
						<td>${consume.date}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</fieldset>
</div>
<script type="text/javascript">
	
		//提交
		function refund() {
			const amount = document.getElementById("amount").value;
			if(amount <= 0){
				return;
			}
			htmlObj = $.ajax({
				cache : true,
				type : "POST",
				url : "consumeManage?requestType=40022&refundId=${cashVo.id}&userId=${cashVo.uid}&amount="+amount,
		//	data : $("#pageForm").serialize(),
			async : false
		});
		var obj = jQuery.parseJSON(htmlObj.responseText);
		if (obj.statusCode != 200) {
			alertMsg.error(obj.message, null);
		} else {
			navTab.reloadFlag("area_list");
			navTab.closeCurrentTab();
			alertMsg.correct(obj.data, null);
		}

	}
</script>
