<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeVo"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusTitle = rb.getString("bike_status_value").split(",");
	String[] errorType = rb.getString("bike_lock_error_value").split(",");//get error type
	int status = -1,errorValue = -1,lockType = -1;
	if (request.getAttribute("bikeVo") != null) {
		BikeVo bikeVo = (BikeVo) request.getAttribute("bikeVo");
		status = bikeVo.getStatus();
		errorValue = bikeVo.getError_status();
		lockType = bikeVo.getBikeType();
	}
	String[] connectStatusValue = rb.getString("bike_connect_status_value").split(",");
	String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
	String[] lockTypeValues = rb.getString("bike_lock_Type_values").split(",");
%>

<div class="pageContent">
	<form method="post" action="bikeManage?requestType=20029"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="isNavTab" value="${isNavTab}" /> <input
			type="hidden" name="id" value="${bikeVo.bid}" />
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("common_id_title")%>：</label> <input
					type="text" size="30" name="number" value="${bikeVo.number}" />
			</div>

			<div class="unit">
				<label>IMEI：</label> <input type="text" name="imei"
					 size="30" value="${bikeVo.imei}" />
			</div>
			<div class="unit">
				<label>MAC：</label> <input type="text" name="mac"
					size="30" value="${mac}" />
			</div>

			<div class="unit">
				<label><%=rb.getString("common_status_title")%>：</label> <select
					name="status">
					<%
						for (int i = 0; i < statusTitle.length - 2; i++) {
					%>
					<option value="<%=i%>" <%if (i == status && status >= 0) {%>
						selected="selected" <%}%>><%=statusTitle[i + 1]%></option>
					<%
						}
					%>
				</select>
			</div>

			<div class="unit">
				<label><%=rb.getString("bike_lock_error_title")%>：</label> <select
					name="error">
					<%
						for (int i = 0; i < errorType.length - 2; i++) {
					%>
					<option value="<%=i%>"
						<%if (i == errorValue && errorValue >= 0) {%> selected="selected"
						<%}%>><%=errorType[i + 1]%></option>
					<%
						}
					%>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_lock_type_title")%>：</label> <select
					name="lockType">
					<%
						for (int i = 1; i < lockTypeValues.length; i++) {
					%>
					<option value="<%=i%>" <%if (i == lockType && lockType >= 0) {%>
						selected="selected" <%}%>><%=lockTypeValues[i]%></option>
					<%
						}
					%>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("bike_type_price") %>：</label> <select
					name="type" id="priceType">
					<c:forEach items="${typeList}" varStatus="status" var="type">
						<option value="${type.id}"
							<c:if test="${type.id == bikeVo.typeId}">selected="selected"</c:if>>
							<td>${type.price}/<c:if test="${type.unit_type == 1}">
									<%= unitType[0] %>
								</c:if> <c:if test="${type.unit_type == 2}">
									<%= unitType[1] %>
								</c:if> <c:if test="${type.unit_type == 3}">
									<%= unitType[2] %>
								</c:if>
							</td>
						</option>
					</c:forEach>
				</select>
			</div>

			<%@ include file="bike_func.jspf"%>
			<%@ include file="../common/page_city.jspf"%>
			<div class="unit">
				<label><%=rb.getString("bike_connect_status_title")%>： <c:if
						test="${connect == 1}"><%=connectStatusValue[0] %></c:if> <c:if
						test="${connect == 0}"><%=connectStatusValue[1] %></c:if> </label>
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
