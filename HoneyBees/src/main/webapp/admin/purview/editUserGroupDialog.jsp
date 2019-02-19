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
		action="purviewManage?requestType=104&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<%@ include file="../common/page_admin_check.jspf"%>
			<input type="hidden" name="id" value="${id}" /> <input type="hidden"
				name="groupIds" id="groupIds" value="${groupIds}" />
			<div class="unit" id="t2">
				<label><%=rb.getString("purview_function_name")%>：</label>
				<ul class="tree treeFolder treeCheck expand" tname="group"
					oncheck="treeclick">
					<li><a ><%=rb.getString("common_all_title")%></a>
						<ul>

							<c:forEach items="${groupList}" varStatus="status" var="group">

								<li><a tvalue="${group.id}"
									<c:if test="${group.checked == true}">checked="checked"</c:if>>
										${group.name}</a></li>


							</c:forEach>

						</ul></li>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

<script src="common/common.js" type="text/javascript"/>

<script type="text/javascript">
	var selectItem = getSelectItemValue("${groupIds}");
	//遍历被选中CheckBox元素的集合 得到Value值    
	function treeclick(json) {
		$(json.items).each(function(i){
			var tempValue = "'"+this.value+"',";
			selectItem = selectItem.replace(tempValue,"");
			if(json.checked == true){
				selectItem = selectItem + tempValue;
			}
		});
		this.groupIds.value = selectItem;
	}
</script>
