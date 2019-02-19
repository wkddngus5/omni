<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();

	String[] ruleType = rb.getString("redpack_rule_type").split(",");//get type
%>
<div class="pageContent">

	<form method="post"
		action="redpackManage?requestType=<c:if test="${ruleVo.id==null}">70003</c:if>
	<c:if test="${ruleVo.id!=null}">70004</c:if>&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="id" value="${ruleVo.id}" />
			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input
					type="text" name="name" value="${ruleVo.name}" size="30" />
			</div>

			<div class="unit">
				<label> <%=rb.getString("common_type_title")%>：
				</label> <select name="type" onchange="typeChange(this)">
					<option value="1"
						<c:if test="${ruleVo.type == 1 }">selected="selected"</c:if>><%=ruleType[0]%></option>
					<option value="2"
						<c:if test="${ruleVo.type == 2 }">selected="selected"</c:if>><%=ruleType[1]%></option>
				</select>

			</div>

			<div id="div_money"
				<c:if test="${ruleVo.type == 2 }">style="display: none;"</c:if>>
				<div class="unit">
					<label><%=rb.getString("redpack_rule_free_use_time")%>：</label> <input
						type="text" name="free_use_time" value="${ruleVo.free_use_time}"
						size="30" />
				</div>

				<div class="unit">
					<label><%=rb.getString("redpack_rule_least_use_time")%>：</label> <input
						type="text" name="least_use_time" value="${ruleVo.least_use_time}"
						size="30" />
				</div>

				<div class="unit">
					<label><%=rb.getString("redpack_rule_max_amount")%>：</label> <input
						type="text" name="max_amount" value="${ruleVo.max_amount}"
						size="30" />
				</div>
			</div>
			<div id="div_coupon"
				<c:if test="${ruleVo.type == 1 }">style="display: none;"</c:if>>
				<div class="unit">
					<label><%=rb.getString("coupon_title")%>：</label> <input
						type="text" name="couponVo.name" value="${ruleVo.couponName}"
						size="30" readonly="readonly" /> <a class="btnLook"
						href="couponManage?requestType=50009&only=1"
						lookupGroup="couponVo">Select</a>
				</div>
				<input type="hidden" name="couponVo.id" size="30"
					value="${ruleVo.coupon_id }" />

				<div class="unit">
					<label><%=rb.getString("redpack_rule_coupon_num")%>：</label> <input
						type="text" name="coupon_num" value="${ruleVo.coupon_num}"
						size="30" />
				</div>

				<div class="unit">
					<label><%=rb.getString("bike_area_title")%>：</label> <input
						type="text" name="areaVo.name" value="${ruleVo.areaName}"
						size="30" readonly="readonly" /> <a class="btnLook"
						href="areaManage?requestType=80001&lookup=1" lookupGroup="areaVo">Select</a>
				</div>
				<input type="hidden" name="areaVo.id" size="30"
					value="${ruleVo.area_ids}" />

				<div class="unit">
					<label><%=rb.getString("redpack_rule_must_in_area")%>：</label> <input
						type="radio" name="must_in_area" value="1"
						<c:if test="${ruleVo.must_in_area == 1 }">checked="checked"</c:if> /><%=rb.getString("common_yes_title")%>
					<input type="radio" name="must_in_area" value="0"
						<c:if test="${ruleVo.must_in_area == 0 }">checked="checked"</c:if> /><%=rb.getString("common_no_title")%>
				</div>
			</div>



			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="start_time" value="${ruleVo.start_time}"
					class="date" value="${nowTime}" dateFmt="yyyy-MM-dd HH:mm"
					readonly="true" class="required" /> <a class="inputDateButton"
					href="javascript:;">Select</a>

			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="end_time" value="${ruleVo.end_time}" class="date"
					value="${nowTime}" dateFmt="yyyy-MM-dd HH:mm" readonly="true"
					class="required" /> <a class="inputDateButton" href="javascript:;">Select</a>
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

<script type="text/javascript">
	function typeChange(selects) {

		if (selects.value == 1) {
			this.div_money.style.display = "";
			this.div_coupon.style.display = "none";
		} else {
			this.div_money.style.display = "none";
			this.div_coupon.style.display = "";
		}

	}
</script>
