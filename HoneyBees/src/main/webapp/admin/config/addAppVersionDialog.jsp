<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String funcId = request.getAttribute("funcId").toString();
%>
<style>
input[type="file"] {
	color: transparent;
}
</style>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
</html>
<div class="pageContent">

	<form method="post"
		action="configManage?requestType=60012&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone)"
		
		>
<!-- enctype="multipart/form-data" -->

		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label> <input
					type="radio" name="type" class="required" value="1"
					checked="checked" onchange="typeChanged(this)" />Android <input
					type="radio" name="type" class="required" value="2"
					onchange="typeChanged(this)" />IOS
			</div>


			<div class="unit">
				<label><%=rb.getString("setting_version_name")%>：</label> <input
					type="text" name="name" size="30" class="required" />
			</div>

			<div class="unit">
				<label><%=rb.getString("setting_version_code")%>：</label> <input
					type="number" name="code" size="5" class="required" />
			</div>

	<%-- 		<div class="unit" id="androidFile">
				<label><%=rb.getString("setting_version_file")%>：</label>

				<div class="upload-wrap">
					<div class="thumbnail"></div>
					<input type="text" id="fileName" name="fileName" size="50"
						value="<c:if test="${newsVo.imageVo.path != null}">${newsVo.imageVo.path}</c:if>"
						readonly="readonly" /> <input type="file" id="file" name="file"
						accept="image/*" onchange="selectFile(this)">
				</div>
			</div> --%>
			<div class="unit" id="iosFile">
				<label><%=rb.getString("setting_version_file")%>：</label>
				<textarea rows="2" cols="50" name="url" class="required"></textarea>

			</div>
			<div class="unit">
				<label><%=rb.getString("common_content_title")%>：</label>
				<textarea rows="5" cols="50" name="content" class="required"></textarea>

			</div>




		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
<script type="text/javascript">
	
	function typeChanged(type) {
/* 		var androidFile = document.getElementById("androidFile");
		var iosFile = document.getElementById("iosFile");
		if(type.value == 1){
			//android
			androidFile.hidden = "";
			iosFile.hidden = "hidden";
		}else if(type.value == 2){
			//ios
			iosFile.hidden = "";
			androidFile.hidden = "hidden";
		} */
	}
	
	function selectFile(file) {
		this.fileName.value = file.value;
	}
</script>
