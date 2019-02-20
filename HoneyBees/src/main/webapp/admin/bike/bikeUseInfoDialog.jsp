<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] unitType = rb.getString("bike_type_unit_value").split(",");//get unit type
%>
<div class="pageContent">

	<div class="pageFormContent" layoutH="58">
		<div class="unit">
			<label><%=rb.getString("common_id_title")%>：${useVo.id}</label>
		</div>

		<div class="unit">
			<label><%=rb.getString("bike_number")%>：${useVo.bikeVo.number}</label>
		</div>

		<div class="unit">
			<label><%=rb.getString("user_phone_title")%>：${useVo.userVo.phone}</label>
		</div>

		<div class="unit">
			<label><%=rb.getString("common_date_start_title")%>：${useVo.startTime}</label>
		</div>

		<div class="unit">
			<label><%=rb.getString("common_date_end_title")%>：${useVo.endTime}</label>
		</div>


	</div>

</div>
