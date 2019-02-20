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
		action="bikeManage?requestType=20103&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone)"
		enctype="multipart/form-data">

		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label>IMEI：</label> <input
					type="text" name="imei" size="30" value="${imei}"
					class="required" readonly="readonly"/>
			</div>
			
			<div class="unit">
				<label><%=rb.getString("common_type_title")%>：</label>
				<input type="radio" name="versionType" value="2" checked="checked" onchange="versionTypeChange(2)"/>IOT
				<input type="radio" name="versionType" value="3" onchange="versionTypeChange(3)"/>CTL
			</div>

			<div class="unit">
				<label>Code：</label> <input
					type="text" id="lockType" name="lockType" size="4" value="8A"
					/>
			</div>

			<div class="unit">
				<label>File：</label>

				<div class="upload-wrap">
					<div class="thumbnail"></div>
					<input type="text" id="fileName" name="fileName" size="50"
						readonly="readonly" /> <input type="file"
						id="file" name="file" accept="/*" onchange="selectFile(this)">
				</div>
			</div>


		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
<script type="text/javascript">
	function selectFile(file) {
		this.fileName.value = file.value;
	}
	
	function versionTypeChange(value){
		if(value == 2){
			this.lockType.value = "8A";
		}else{
			this.lockType.value = "20";
		}
	}
</script>
