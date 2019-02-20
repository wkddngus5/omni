<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] couponType = rb.getString("coupon_type_value").split(",");//get type
%>
<div class="pageContent">

	<form method="post" action="bikeManage?requestType=20015"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${industry.id}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>Deposit(<td>${industry.currency}</td>)：</label> <input type="text" name="deposit" size="30" value = "${industry.deposit}"
					class="required" />
			</div>

		
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
