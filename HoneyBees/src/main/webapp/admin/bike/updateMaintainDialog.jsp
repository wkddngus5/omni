<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java"
	import="com.pgt.bikelock.vo.admin.BikeMaintainVo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
	String[] maintainType = rb.getString("bike_maintain_type").split(
			",");//get maintain type
	String[] statusType = rb.getString("common_status_value")
			.split(",");//get status type

	BikeMaintainVo maintainVo = new BikeMaintainVo();
	if (request.getAttribute("maintainVo") != null) {
		maintainVo = (BikeMaintainVo) request
				.getAttribute("maintainVo");
	}
%>
<div class="pageContent">

	<form method="post"
		action="bikeManage?requestType=20033&funcId=<%=funcId%>&id=${maintainVo.id}"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("bike_number")%>：</label> <input type="text"
					name="bikeVo.number" value="${maintainVo.number}" size="30"
					class="required" readonly="readonly" /> <a class="btnLook"
					href="bikeManage?requestType=20021" lookupGroup="bikeVo">Select</a>
			</div>
			<input type="hidden" name="bikeVo.id" class="required"
				value="${maintainVo.bid}" />

			<div class="unit">
				<label><%=rb.getString("nickname_title")%>：</label> <input
					type="text" name="adminVo.name" value="${maintainVo.nickname}"
					size="30" class="required" readonly="readonly" /> <a
					class="btnLook" href="adminManage?requestType=10008"
					lookupGroup="adminVo">Select</a>
			</div>
			<input type="hidden" name="adminVo.id" class="required"
				value="${maintainVo.admin_id}" />

			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label> <select
					name="type">
					<%
						for (int i = 1; i < maintainType.length; i++) {
					%>
					<option value="<%=i%>" <%if (maintainVo.getType() + 1 == i) {%>
						selected="selected" <%}%>>
						<%=maintainType[i]%>
					</option>
					<%
						}
					%>
				</select>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_status_title")%>：</label> <input
					type="radio" name="status" value="0"
					<c:if test="${maintainVo.status == 0 }">checked="checked"</c:if> /><%=statusType[1]%>
				<input type="radio" name="status" value="1"
					<c:if test="${maintainVo.status == 1 }">checked="checked"</c:if> /><%=statusType[2]%>

			</div>
			<div class="unit">
				<label><%=rb.getString("common_note_title")%>：</label>
				<textarea rows="5" cols="50" name="note">${maintainVo.note}</textarea>
			</div>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
