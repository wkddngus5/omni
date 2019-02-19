<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="purviewManage?requestType=102&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input
					type="text" size="30" class="required" value="${groupVo.name}"
					name="name" />
			</div>
			<div class="unit">
				<label><%=rb.getString("common_note_title")%>：</label> <input
					type="text" size="30" class="required" value="${groupVo.note}"
					name="note" />
			</div>

			<%@ include file="../common/page_admin_check.jspf"%>

			<input type="hidden" name="id" value="${groupVo.id}" /> <input
				type="hidden" name="funcIds" id="funcId" value="${funcIds}" /> <input
				type="hidden" name=notifyTypes id="notifyTypes"
				value="${notifyTypes}" />
			<div class="unit" id="t2">
				<label><%=rb.getString("purview_function_group_name")%>：</label>
				<ul class="tree treeFolder treeCheck" oncheck=funcTreeclick>
					<li><a href="javascript:;"><%=rb.getString("common_all_title")%></a>
						<ul>

							<c:forEach items="${funcList}" varStatus="status" var="func">

								<li><a href="javascript:;" tname="funcId"
									tvalue="${func.id}"
									<c:if test="${func.checked == true}">checked="checked"</c:if>>
										${func.name}</a> <c:forEach items="${func.subList}" var="sub">
										<ul>
											<li><a href="javascript:;" tname="funcId"
												<c:if test="${sub.checked == true}">checked="checked"</c:if>
												tvalue="${sub.id}" pvalue="${func.id}">${sub.name}</a> <c:forEach
													items="${sub.subList}" var="third">
												
													<li><a href="javascript:;" tname="funcId"
														<c:if test="${third.checked == true}">checked="checked"</c:if>
														tvalue="${third.id}">${sub.name}>${third.name}</a></li>
													
												</c:forEach></li>
										</ul>
									</c:forEach></li>


							</c:forEach>

						</ul></li>
			</div>

			<div class="unit" id="t3">
				<label><%=rb.getString("notification_management_title")%>：</label>
				<ul class="tree treeFolder treeCheck" oncheck="notifyTreeclick">
					<li><a href="javascript:;"><%=rb.getString("common_all_title")%></a>
						<ul>
							<c:forEach items="${notifyList}" varStatus="status" var="notify">

								<li><a href="javascript:;" tname="notifyType"
									tvalue="${status.count}"
									<c:if test="${notify.checked == true}">checked="checked"</c:if>>
										${notify.name}</a></li>
							</c:forEach></ui>
				</li>

				</ul>
				
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

<script src="common/common.js" type="text/javascript"/>
<script type="text/javascript">
	var selectFuncItem = getSelectItemValue("${funcIds}");
	var selectNotifyItem = getSelectItemValue("${notifyTypes}");  
	
	//遍历被选中CheckBox元素的集合 得到Value值    
	function funcTreeclick(json) {
		$(json.items).each(function(i){
			var tempValue = "'"+this.value+"',";
			selectFuncItem = selectFuncItem.replace(tempValue,"");
			if(json.checked == true){
				selectFuncItem = selectFuncItem + tempValue;
			}
		});
		this.funcId.value = selectFuncItem;
		//alert(this.funcId.value);    
	}

	function notifyTreeclick(json) {
		$(json.items).each(function(i){
			var tempValue = "'"+this.value+"',";
			selectNotifyItem = selectNotifyItem.replace(tempValue,"");
			if(json.checked == true){
				selectNotifyItem = selectNotifyItem + tempValue;
			}
		});
		this.notifyTypes.value = selectNotifyItem;
		//alert(this.notifyTypes.value);    
	}
</script>
