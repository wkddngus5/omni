<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] couponType = rb.getString("coupon_type_value").split(",");//get type
%>
<div class="pageContent">

	<form method="post" action="couponManage?requestType=50003"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="id" value="${couponVo.id}" />
			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label>

				<c:if test="${couponVo.type==1}">
					<%=couponType[0]%>
				</c:if>
				<c:if test="${couponVo.type==2}">
					<%=couponType[1]%>
				</c:if>
				<c:if test="${couponVo.type==3}">
					<%=couponType[2]%>
				</c:if>
				<c:if test="${couponVo.type==4}">
					<%=couponType[3]%>
				</c:if>
				<input type="hidden" name="typeId" value="${couponVo.type}" />
			</div>
			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input
					type="text" name="name" value="${couponVo.name}" size="30"
					class="required" />
			</div>

			<c:if test="${couponVo.type != 4}">
				<div class="unit">

					<label id="value_title"><%=rb.getString("consume_order_amount_title")%>：</label>
					<input type="text" name="value" size="30" class="required"
						value="${couponVo.value}" />
				</div>
			</c:if>

			<c:if test="${couponVo.type == 2}">
				<div class="unit" id="div_repeat">
					<label><%=rb.getString("coupon_repeat_use")%>：</label>
					<c:if test="${couponVo.repeat == 0 }">
						<input type="radio" name="repeat" value="0" checked="checked" />
						<%=rb.getString("common_no_title")%>
						<input type="radio" name="repeat" value="1" />
						<%=rb.getString("common_yes_title")%>
					</c:if>
					<c:if test="${couponVo.repeat == 1 }">
						<input type="radio" name="repeat" value="0" checked="checked" />
						<%=rb.getString("common_no_title")%>
						<input type="radio" name="repeat" value="1" checked="checked" />
						<%=rb.getString("common_yes_title")%>
					</c:if>

				</div>
			</c:if>
			<c:if test="${couponVo.type == 3}">
				<div class="unit" id="div_day">
					<label><%=rb.getString("common_count_title")%>：</label> <input
						type="text" name="day" id="day" size="30" class="required"
						value="${couponVo.day}" />
				</div>
			</c:if>


			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="start" class="date"
					value="${couponVo.start_time}" dateFmt="yyyy-MM-dd HH:mm"
					readonly="true" class="required" /> <a class="inputDateButton"
					href="javascript:;">Select</a>
			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="end" class="date" value="${couponVo.end_time}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("user_area_title")%>：</label> <select
					name="cityId">
					<option value="0"><%=rb.getString("common_all_title")%></option>
					<c:forEach items="${cityList}" varStatus="status" var="city">
						<option value="${city.id}"
							<c:if test="${city.id == couponVo.cityId}">selected="selected"</c:if>><td>${city.name}</td>
						</option>
					</c:forEach>

				</select>
			</div>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit"><%=rb.getString("common_commit_title")%></button>
						</div>
					</div></li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close"><%=rb.getString("common_cancle_title")%></button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>
</div>

