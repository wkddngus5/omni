<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] couponType = rb.getString("coupon_type_value").split(",");//get type
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post"
		action="couponManage?requestType=50002&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label> <%=rb.getString("common_type_title")%>：
				</label> <select name="typeId" onchange="typeChange(this)">
					<option value="1"><%=couponType[0]%></option>
					<option value="2"><%=couponType[1]%></option>
					<option value="3"><%=couponType[2]%></option>
					<option value="4"><%=couponType[3]%></option>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input
					type="text" name="name" size="30" class="required" />
			</div>

			<div class="unit" id="div_amount">

				<label id="value_title"><%=rb.getString("consume_order_amount_title")%>：</label>
				<input type="text" name="value" size="30" class="" />
			</div>

			<div class="unit" id="div_day" style="display:none">
				<label><%=rb.getString("common_count_title")%>：</label> <input
					type="text" name="day" id="day" size="30" class="" />
			</div>
			<div class="unit" id="div_repeat" style="display:none">
				<label><%=rb.getString("coupon_repeat_use")%>：</label> <input
					type="radio" name="repeat" value="0" onclick="repeatChange(this)" />
				<%=rb.getString("common_no_title")%>
				<input type="radio" name="repeat" value="1"
					onclick="repeatChange(this)" />
				<%=rb.getString("common_yes_title")%>
			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="start" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>

			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="end" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>
			</div>

			<div class="unit">
				<label><%=rb.getString("user_area_title")%>：</label> <select
					name="cityId">
					<option value="0"><%=rb.getString("common_all_title")%></option>
					<c:forEach items="${cityList}" varStatus="status" var="city">
						<option value="${city.id}"
							<c:if test="${city.id == cityId}">selected="selected"</c:if>><td>${city.name}</td>
						</option>
					</c:forEach>

				</select>
			</div>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
<script type="text/javascript">
	function typeChange(selects){
		<%-- if(selects.value == 1){
			this.value_title.innerHTML = "<%=rb.getString("common_percent_title")%>(0-1)";
		}else{
			this.value_title.innerHTML = <%=rb.getString("consume_order_amount_title")%>;
		} --%>
		
		if(selects.value == 2){
			this.div_repeat.style.display = "";			
		}else{
			this.div_repeat.style.display = "none";
		}
		
		if(selects.value == 3){
			this.div_day.style.display = "";			
		}else{
			this.div_day.style.display = "none";
		}
		
		if(selects.value == 4){
			this.div_amount.style.display = "none";
		}else{		
			this.div_amount.style.display = "";		
		}
	}
</script>
