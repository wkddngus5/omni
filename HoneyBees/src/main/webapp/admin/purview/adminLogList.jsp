<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "adminManage?requestType=10006";
	boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>

<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
	method="post">
	<input type="hidden" name="funcIds" id="funcIds" value="${funcIds}" />
	<div id="t2"
		style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:95%; border:solid 1px #CCC; line-height:21px; background:#FFF;">
		<ul class="tree treeFolder treeCheck expand" oncheck="treeclick">
			<li><%=rb.getString("common_all_title")%></li>
			<c:forEach items="${funcList}" varStatus="status" var="func">

				<li><a href="javascript:;" tname="funcId" tvalue="${func.id}"
					<c:if test="${func.checked == true}">checked="checked"</c:if>>
						${func.name}</a> <c:forEach items="${func.subList}" var="sub">
						<ul>
							<li><a href="javascript:;" tname="funcId"
								<c:if test="${func.checked == true}">checked="checked"</c:if>
								tvalue="${sub.id}">${sub.name}</a> <c:forEach
									items="${sub.subList}" var="third">

									<li><a href="javascript:;" tname="funcId"
										<c:if test="${third.checked == true}">checked="checked"</c:if>
										tvalue="${third.id}">${third.name}</a></li>

								</c:forEach></li>
						</ul>
					</c:forEach></li>


			</c:forEach>

		</ul>
	</div>
	<div class="pageHeader">

		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("common_keywords_title") %>" /></td>

					<td><%=rb.getString("common_date_title")%>： <input type="text"
						name="startTime" class="date" value="${startTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> -
						<input type="text" name="endTime" class="date" value="${endTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /></td>

				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>

	</div>

</form>

<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>

	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("login_username")%></th>
				<th width="120"><%=rb.getString("nickname_title")%></th>
				<th width="120"><%=rb.getString("purview_function_name")%></th>
				<th width="120"><%=rb.getString("common_data")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${LogList}" varStatus="status" var="log">
				<!-- target 在删除和修改的时候用到，选择项的ID -->
				<tr target="id_item" rel="${log.id}">
					<td>${log.id}</td>
					<td>${log.adminVo.username}</td>
					<td>${log.adminVo.nickname}</td>
					<td>${log.funcName}</td>
					<td>${log.dataId}</td>
					<td>${log.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>

<script type="text/javascript">
	//遍历被选中CheckBox元素的集合 得到Value值    
	function treeclick() {
		var oidStr = ""; //定义一个字符串用来装值的集合    

		//jquery循环t2下的所有选中的复选框 
		$("#t2 input:checked").each(function(i, a) {
			//alert(a.value);    
			oidStr += a.value + ','; //拼接字符串    
		});
		this.funcIds.value = oidStr;
		//alert(this.funcIds.value);    
	}
</script>