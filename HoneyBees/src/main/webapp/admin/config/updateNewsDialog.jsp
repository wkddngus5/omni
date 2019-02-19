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
		action="configManage?requestType=60004&funcId=<%=funcId%>"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone)"
		enctype="multipart/form-data">
		<input type="hidden" name="id" value="${newsVo.id}"> <input
			type="hidden" name="thumb" value="${newsVo.thumb}">

		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label><%=rb.getString("common_name_title")%>：</label> <input
					type="text" name="title" size="30" value="${newsVo.title}"
					class="required" />
			</div>

			<div class="unit">
				<label><%=rb.getString("common_image_title")%>：</label>

				<div class="upload-wrap">
					<div class="thumbnail"></div>
					<input type="text" id="fileName" name="fileName" size="50"
						value="<c:if test="${newsVo.imageVo.path != null}">${newsVo.imageVo.path}</c:if>"
						class="required" readonly="readonly" /> <input type="file"
						id="file" name="file" accept="image/*" onchange="selectFile(this)">
				</div>
			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_start_title")%>：</label> <input
					type="text" name="start" class="date" value="${newsVo.start_time}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>

			</div>

			<div class="unit">
				<label><%=rb.getString("common_date_end_title")%>：</label> <input
					type="text" name="end" class="date" value="${newsVo.end_time}"
					dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> <a
					class="inputDateButton" href="javascript:;">Select</a>
			</div>
			<%@ include file="../common/page_city.jspf"%>
			<div class="unit">
				<textarea class="editor" name="content" rows="23" cols="100"
					upLinkUrl="upload" upLinkExt="zip,rar,txt" upImgUrl="upload"
					upImgExt="jpg,jpeg,gif,png" upFlashUrl="upload" upFlashExt="swf"
					upMediaUrl="upload" upMediaExt:"avi" lang="en.js">${newsVo.content}</textarea>
			</div>

		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
<script type="text/javascript">
	function selectFile(file) {
		this.fileName.value = file.value;
	}
</script>
