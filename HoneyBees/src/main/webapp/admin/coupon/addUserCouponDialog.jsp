<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">

	<form method="post"
		action="couponManage?requestType=50006&type=${type}&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<c:if test="${type == 0 }">
				<div class="unit">
					<label><%=rb.getString("user_title")%>：</label> <input type="text"
						name="userVo.name" size="30" class="required" readonly="readonly" />
					<a class="btnLook" href="userManage?requestType=30002"
						lookupGroup="userVo">Select</a>
				</div>
				<div class="unit">
					<label><%=rb.getString("user_id_title")%>：</label> <input
						type="text" name="userVo.id" size="30" class="required"
						readonly="readonly" />
				</div>

			</c:if>
			<c:if test="${type == 1 }">
			
				<div class="unit">
					<label><%=rb.getString("coupon_same_code_title")%>：</label> 
					<input type="radio" name="sameCode" class="required" value="1"/><%=rb.getString("common_yes_title")%>
					<input type="radio" name="sameCode" class="required" value="0"/><%=rb.getString("common_no_title")%>
				</div>
				<div class="unit">
					<label><%=rb.getString("common_count_title")%>：</label> <input
						type="text" name="count" size="30" class="required" />

				</div>
			</c:if>

			<div class="unit">
				<label><%=rb.getString("coupon_title")%>：</label> <input type="text"
					name="couponVo.name" size="30" class="required" readonly="readonly" />
				<a class="btnLook" href="couponManage?requestType=50009&only=1"
					lookupGroup="couponVo">Select</a>
			</div>
			<div class="unit">
				<label><%=rb.getString("coupon_id_title")%>：</label> <input
					type="text" name="couponVo.id" size="30" class="required"
					readonly="readonly" />
			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="couponVo.start" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>

			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="couponVo.end" class="date" value="${nowTime}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>
			</div>

			<input type="hidden" name="couponVo.value" size="30" class="required" />
			<input type="hidden" name="couponVo.type" size="30" class="required" />
			
			<%@ include file="../common/page_admin_check.jspf"%>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
