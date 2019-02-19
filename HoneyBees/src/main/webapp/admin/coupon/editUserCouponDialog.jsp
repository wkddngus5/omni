<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
%>
<div class="pageContent">

	<form method="post" action="couponManage?requestType=50007"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			
			<input type="hidden" name="id" value="${couponVo.id}" />
	
			<div class="unit">
				<label><%=rb.getString("user_title") %>：</label> 
				
				<input type="text" name="userVo.name" size="30"
					class="required" readonly="readonly" value="${couponVo.userVo.phone}"/>
					<a class="btnLook" href="userManage?requestType=30002&only=1" lookupGroup="userVo">Select</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("user_id_title") %>：</label> 
				
				<input type="text" name="userVo.id" size="30"
					class="required" readonly="readonly" value="${couponVo.uid}"/>
			</div>

			<div class="unit">
				<label><%=rb.getString("coupon_title") %>：</label> <input type="text" name="couponVo.name" size="30"
					class="required" readonly="readonly" value="${couponVo.couponVo.name}"/>
					<a class="btnLook" href="couponManage?requestType=50009&only=1" lookupGroup="couponVo">Select</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("coupon_id_title") %>：</label> <input type="text" name="couponVo.id" size="30"
					class="required" readonly="readonly" value="${couponVo.couponVo.id}"/>
			</div>
			<input type="hidden" name="couponVo.start" size="30" class="required" />
			<input type="hidden" name="couponVo.end" size="30" class="required" />
			<input type="hidden" name="couponVo.value" size="30" class="required" />
			
			<%@ include file="../common/page_admin_check.jspf"%>
			
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
