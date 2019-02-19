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
		<legend><%=rb.getString("consume_order_detail_info")%></legend>
		<dl>
			<dt><%=rb.getString("common_id_title")%>：
			</dt>
			<dd>${tradeVo.id}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("user_title")%>：
			</dt>
			<dd>
				<a href="userManage?requestType=30006&id=${consume.uid}"
					target="navTab">${tradeVo.userVo.phone}</a>
			</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("consume_order_amount_title")%>：
			</dt>
			<dd>${tradeVo.amount}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("user_money_title")%>：
			</dt>
			<dd>${tradeVo.balance}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_type_title")%>：
			</dt>
			<dd>${tradeVo.typeStr}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("consume_order_pay_way_title")%>：
			</dt>
			<dd>${tradeVo.wayStr}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_status_title")%>：
			</dt>
			<dd>${tradeVo.statusStr}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("consume_order_pay_out_id")%>：
			</dt>
			<dd>${tradeVo.out_trade_no}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_date_title")%>：
			</dt>
			<dd>${tradeVo.date}</dd>
		</dl>
	</fieldset>

	<c:if test="${couponVo != null}">
		<fieldset>
		<legend><%=rb.getString("consume_order_link_pay_coupon_info")%></legend>
		<dl>
			<dt><%=rb.getString("common_id_title")%>：
			</dt>
			<dd>${couponVo.id}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_name_title")%>：
			</dt>
			<dd>${couponVo.couponVo.name}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("consume_order_amount_title")%>：
			</dt>
			<dd>${couponVo.amount}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_date_start_title")%>：
			</dt>
			<dd>${couponVo.start_time}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_date_end_title")%>：
			</dt>
			<dd>${couponVo.end_time}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("coupon_code_title")%>：
			</dt>
			<dd>${couponVo.code}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_date_title")%>：
			</dt>
			<dd>${couponVo.date}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("coupon_active_date_title")%>：
			</dt>
			<dd>${couponVo.active_date}</dd>
		</dl>
	</fieldset>
	</c:if>


</div>
