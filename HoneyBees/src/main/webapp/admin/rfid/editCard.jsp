<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
	
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
	
	String[] status = rb.getString("rfid_card_status_value").split(",");//get status
%>
<div class="pageContent">
	<form method="post"
		action="rfidManage?requestType=102&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="id" value="${cardVo.id}"/>
			<div class="unit">
				<label><%=rb.getString("rfid_card_id")%>：</label> <input type="text"
					size="30" value="${cardVo.cardId}" name="cardId" />
			</div>
			<div class="unit">
				<label><%=rb.getString("rfid_card_no")%>：</label> <input type="text"
					size="30" value="${cardVo.cardNo}" name="cardNo" />
			</div>
			<div class="unit">
				<label><%=rb.getString("user_title")%>：</label> <input type="text"
					name="userVo.phone" size="30" class="required" readonly="readonly" 
					<c:if test="${cardVo.userVo != null }">value = "${cardVo.userVo.phone}"</c:if>
					/>
				<a class="btnLook" href="userManage?requestType=30002"
					lookupGroup="userVo">Select</a>
				<input
					type="hidden" name="userVo.id" size="30" class="required"
					readonly="readonly" value="${cardVo.uid}"/>
			</div>
			
			<div  class="unit">
				<label><%=rb.getString("common_status_title")%>：</label>
				<select name="status">
					<option value="0" <c:if test="${cardVo.status == 0 }">selected="selected"</c:if>><%= status[1] %></option>
					<option value="1" <c:if test="${cardVo.status == 1 }">selected="selected"</c:if>><%= status[2] %></option>
					<option value="2" <c:if test="${cardVo.status == 2 }">selected="selected"</c:if>><%= status[3] %></option>
				</select>
			</div>
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

