<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post" action="messageManage?requestType=90004"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<input type="hidden" name="id" value="${messageVo.id}" />

			<table>
				<tr height="20">
					<c:if test="${messageVo.from_type =='admin' }">
						<td style="color: blue;">${messageVo.adminName}</td>
					</c:if>
					<c:if test="${messageVo.from_type =='user' }">
						<td><a style="color: blue;">${messageVo.userPhone}</a></td>
					</c:if>
					<td style="color:gray;">${messageVo.date}</td>
				</tr>
				<tr height="20">
					<td colspan="2"><%=rb.getString("common_title")%>:${messageVo.title}</td>
				</tr>

				<tr>
					<td colspan="2"><%=rb.getString("common_content_title")%>:${messageVo.content}</td>
				</tr>

				<tr height="10">
					<td colspan="2"></td>
				</tr>

				<c:forEach items="${messageList}" varStatus="status" var="message">
					<tr>
						<c:if test="${message.from_type =='admin' }">
							<td style="color: blue;">${message.adminName}</td>
						</c:if>
						<c:if test="${message.from_type =='user' }">
							<td><a style="color: blue;">${message.userPhone}</a></td>
						</c:if>
						<td style="color:gray;">${message.date}</td>
					</tr>
					<tr height="20">
						<td colspan="2">${message.replyContent}</td>
					</tr>
					<tr height="10">
						<td colspan="2"></td>
					</tr>
				</c:forEach>

				<tr>
					<td colspan="2"><textarea rows="4" cols="45" name="content"
						class="required"	placeholder="<%=rb.getString("common_input_tips_title")%>"></textarea></td>
				</tr>


			</table>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
