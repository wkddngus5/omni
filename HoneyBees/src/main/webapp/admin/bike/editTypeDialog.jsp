<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeTypeVo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
	BikeTypeVo typeVo = new BikeTypeVo();
	if (request.getAttribute("typeVo") != null) {
		typeVo = (BikeTypeVo) request.getAttribute("typeVo");
	}
%>
<div class="pageContent">

	<form method="post" action="bikeManage?requestType=20016"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="id" value="${typeVo.id}" />
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("bike_type_unit")%>：</label> <select
					name="type">
					<%
						for (int i = 0; i < unitType.length; i++) {
					%>
					<option value=<%=i+1%> <%if (typeVo.getUnit_type() == i+1) {%>
						selected="selected" <%}%>><%=unitType[i]%></option>
					<%
						}
					%>
				</select>
			</div>

			<div class="unit">
				<label><%=rb.getString("bike_type_count")%> ：</label> <input
					type="text" name="count" value="${typeVo.count}" size="30"
					class="required" />
			</div>

			<div class="unit">
				<label><%=rb.getString("bike_type_price")%>：</label> <input
					type="text" name="price" value="${typeVo.price}" size="30"
					class="required" />
			</div>


			<!-- Hold -->
			<div class="unit">
				<label>Hold <%=rb.getString("bike_type_unit")%>：</label>
				<select name="holdUnitType">
					<%
						for (int i = 0; i < unitType.length; i++) {
					%>
					<option value=<%=i+1%> <%if (typeVo.getHoldUnitType() == i+1) {%>
						selected="selected" <%}%>><%=unitType[i]%></option>
					<%
						}
					%>
				</select>
			</div>

			<div class="unit">
				<label>Hold <%=rb.getString("bike_type_count")%> ：</label>
				<input
					type="text" name="holdCount" value="${typeVo.holdCount}" size="30"
					class="required" />
			</div>

			<div class="unit">
				<label>Hold <%=rb.getString("bike_type_price")%>：</label> <input
					type="text" name="holdPrice" value="${typeVo.holdPrice}" size="30"
					class="required" />
			</div>


			<div class="unit">
				<label>Hold Max <%=rb.getString("bike_type_count")%> ：</label>
				<input
					type="text" name="holdMaxCount" value="${typeVo.holdMaxCount}" size="30"
					class="required" />
			</div>



			<%@ include file="../common/page_city.jspf"%>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
