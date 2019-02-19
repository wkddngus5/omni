<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil" %>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] typeValue = rb.getString(
			"notification_management_config_type").split(",");
	int type = Integer
			.parseInt(request.getAttribute("type").toString());
	String funcId = request.getAttribute("funcId").toString();
%>
<div class="pageContent">
	<form method="post"
		action="notifyManage?requestType=11008&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" value="${configVo.id}" name="id" />
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label> <select
					name="type" onchange="typeChanged(this)"  >	<%
						for (int i = 1; i < typeValue.length; i++) {
					%>
					<option value=<%=i%> <%if (type == i) {%> selected="selected" <%}%>><%=typeValue[i]%></option>
					<%
						}
					%>
				</select>
			</div>
			<div id="defaultValueDiv" <c:if test="${configVo.type != 3 && configVo.type != 4 && configVo.type != 6}">hidden="hidden" </c:if> >
			<div class="unit">
				<label id="defaultTitle">
					<c:if test="${configVo.type == 3}"><%=rb.getString("notification_management_config_heart_interval")%></c:if>
					<c:if test="${configVo.type == 4}"><%=rb.getString("notification_management_config_location_frequency")%></c:if>
					<c:if test="${configVo.type == 6}"><%=rb.getString("notification_management_config_lowpower_value")%></c:if>
					：</label>
				<input type="number" name="defaultValue" id="defaultValue" value="${configVo.jsonValue}"/>
			</div>
			<div class="unit">
				<label><%=rb
					.getString("notification_management_config_monitoring_frequency")%>：</label>
				<input type="number" name="frequency" id="frequency" value="${configVo.jsonFrequency}"/>
			</div>
			</div>
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
				<textarea rows="7" cols="50" name="content" id="content">${configVo.template}</textarea>
				Tag</br>
				</br><a href="#" onclick="addTagToTemplate('admin');"><font style="color: blue;">{admin}</font></a></br></br>
				<a href="#" onclick="addTagToTemplate('user');"><font style="color: blue;">{user}</font></a></br></br>
				<a href="#" onclick="addTagToTemplate('bike');"><font style="color: blue;">{bike}</font></a></br></br>
				<a href="#" onclick="addTagToTemplate('UnNormalCount');"><font style="color: blue;">{UnNormalCount}</font></a>
			</div>
			<div class="unit">
				<label><%=rb
					.getString("notification_management_config_sms_notification")%>：</label>
				<input type="radio" name="sms" value="0"  <c:if test="${configVo.sms == 0}">checked="checked"</c:if> /><%=rb.getString("common_no_title")%>
				<input type="radio" name="sms" value="1" <c:if test="${configVo.sms == 1}">checked="checked"</c:if> /><%=rb.getString("common_yes_title")%>
			</div>
			<div class="unit">
				<label><%=rb
					.getString("notification_management_config_email_notification")%>：</label>
				<input type="radio" name="email" value="0" <c:if test="${configVo.email == 0}">checked="checked"</c:if> /><%=rb.getString("common_no_title")%>
				<input type="radio" name="email" value="1" <c:if test="${configVo.email == 1}">checked="checked"</c:if>  /><%=rb.getString("common_yes_title")%>
			</div>
			<%@ include file="../common/page_admin_check.jspf"%>

			<div class="unit">
				<font style="color: red;"><%=rb
					.getString("notification_management_template_edit_tips")%></font>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>

<script type="text/javascript">
	function addTagToTemplate(tag){
		const content = document.getElementById("content");
		content.value = content.value + "{"+tag+"}";
	}

	
	function typeChanged(tag){
		
		const value = tag.value;
		if(value == 3 || value == 4 || value == 6){
			var title = document.getElementById("defaultTitle");
			var defaultValue = document.getElementById("defaultValue");
			var frequency = document.getElementById("frequency");
			if(value == 3){
				title.innerHTML = "<%=rb.getString("notification_management_config_heart_interval")%>:";
				defaultValue.value = ${heart_value};
				frequency.value =  ${heart_frequency };
			}else if(value == 4){
				title.innerHTML = "<%=rb.getString("notification_management_config_location_frequency")%>:";
				defaultValue.value = ${location_value };
				frequency.value =  ${location_frequency };
			}else if(value == 6){
				title.innerHTML = "<%=rb.getString("notification_management_config_lowpower_value")%>:";
				defaultValue.value = ${ power_value };
				frequency.value =  ${power_frequency};
			}
			document.getElementById("defaultValueDiv").hidden = "";
		}else{
			document.getElementById("defaultValueDiv").hidden = "hidden";
		}
	}
</script>
