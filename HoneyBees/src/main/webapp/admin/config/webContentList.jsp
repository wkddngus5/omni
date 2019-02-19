<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.WebContentVo" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] webType = rb.getString("setting_webcontent_type_value").split(
			",");//get web type
			String actionUrl = "configManage?requestType=60001";
	boolean export = false;
%>

<div class="pageHeader"></div>
<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit"
				href="configManage?requestType=60002&id={ag_id}" target="dialog"
				rel="dialog_edit_type" width="800" height="480" mask="true"><span><%=rb.getString("common_update_title") %></span></a></li>
		</ul>
	</div> --%>
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_title")%></th>
				<%-- <th width="120"><%=rb.getString("common_date_title")%></th> --%>
				<th width="120"><%=rb.getString("common_preview_title")%></th>
			</tr>
		</thead>
		<tbody>
			<%
			List<WebContentVo> list = ((List<WebContentVo>)request.getAttribute("web_list"));
				for (int i =0;i<list.size(); i++) {
				WebContentVo contentVo = list.get(i);
			 %>
			 <tr target="id_item" rel="<%= contentVo.getId() %>">
					<td><%=contentVo.getId() %></td>
					<td><% if(contentVo.getType() > 0){%> <%=webType[contentVo.getType()-1] %> <%} %></td>
					<%-- <td><%= contentVo.getTitle() %></td> --%>
					<td><%= contentVo.getDate() %></td>
					<td><a
						href="<%=basePath%>other?requestType=50001&industryId=<%= contentVo.getIndustry_id() %>
						&type=<%= contentVo.getType() %>"
						target="dialog" mask="true"><%=rb.getString("common_preview_title")%></a></td>
				</tr>
			 <% }%>
		</tbody>
	</table>
	<div class="panelBar"></div>

</div>