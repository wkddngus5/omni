<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pageContent">
	<form method="post" action="setIpAddressDialog" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
		   <div class="unit">
				<label>单车编号：</label>
				<input type="text" name="bikeNo" size="30"   class="required" value="${bikeNo}" readonly="readonly"  />
			</div>
			
			<div class="unit">
				<label>IMEI号：</label>
				<input type="text" name="IMEI" size="30"   class="required"  value="${imei}"  readonly="readonly" />
			</div>
			<div class="unit">
				<label>地址类型(0-ip,1-域名)：</label>
				<input type="text" name="mode" size="30"   class="required"     />
			</div>
			 <div class="unit">
				<label>IP地址：</label>
				<input type="text" class="required" name="ip"     />
			 
			</div>
			<div class="unit">
				<label>端口号：</label>
				<input type="text" class="required" name="port"     />
			</div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
