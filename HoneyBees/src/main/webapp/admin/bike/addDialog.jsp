<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
	String[] lockTypeValues = rb.getString("bike_lock_Type_values")
			.split(",");
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="bikeManage?requestType=20002&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("common_id_title")%>：</label> <input
					type="text" name="number" size="30" class="required" />
			</div>

			<div class="unit">
				<label>IMEI：</label> <input type="text" name="imei" size="30"
					class="required" />
			</div>
			<div class="unit">
				<label>MAC：</label> <input type="text" name="mac"
					size="30" />
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_lock_type_title")%>：</label> <select
					name="lockType" id="lockType">
					<%
						for (int i = 1; i < lockTypeValues.length; i++) {
					%>
					<option value=<%=i%> <%if(i==2){ %> selected="selected" <%} %>><%=lockTypeValues[i]%></option>
					<%
						}
					%>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_type_price")%>：</label> <select
					name="typeId" id="priceType">
					<c:forEach items="${typeList}" varStatus="status" var="type">
						<option value="${type.id}">
							<td>${type.price}/<c:if test="${type.unit_type == 1}">
									<%=unitType[0]%>
								</c:if> <c:if test="${type.unit_type == 2}">
									<%=unitType[1]%>
								</c:if> <c:if test="${type.unit_type == 3}">
									<%=unitType[2]%>
								</c:if>
							</td>
						</option>
					</c:forEach>
				</select>
			</div>

			<%@ include file="bike_func.jspf"%>
			<%@ include file="../common/page_city.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
