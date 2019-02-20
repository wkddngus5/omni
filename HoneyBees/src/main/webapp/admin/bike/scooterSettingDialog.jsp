<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeVo"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] speedMode = rb.getString("bike_extend_mode_values").split(
			",");
	String[] settingsMode = rb.getString("bike_extend_settings").split(
			",");
	String[] voiceType = rb.getString("bike_lock_voie_type").split(",");
	String[] startType = rb.getString(
			"bike_extend_speed_setings_start_type_values").split(",");
	String[] ipMode = rb.getString("bike_extend_adress_mode_value")
			.split(",");
	String funcId = request.getAttribute("funcId").toString();
	String type = request.getAttribute("type").toString();

	String extend = (String) request.getAttribute("extendInfo");
	JSONObject extendInfo = extend != null ? JSONObject
			.parseObject(extend) : null;
%>
<div class="pageContent">
	<form method="post"
		action="bikeManage?requestType=20102&funcId=<%=funcId%>&type=<%=type%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<input type="hidden" name="imei" value="${imei}" />
		<div class="pageFormContent" layoutH="58">
			<c:if test="${type == 'IOT' }">
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_iot_settings_accelerometer_sensitivity")%>：</label>
					<input type="radio" name="sensitivity" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY) == 0) {%>
						checked="checked" <%}%> /><%=speedMode[0]%>
					<input type="radio" name="sensitivity" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY) == 1) {%>
						checked="checked" <%}%> /><%=speedMode[1]%>
					<input type="radio" name="sensitivity" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY) == 2) {%>
						checked="checked" <%}%> /><%=speedMode[2]%>
					<input type="radio" name="sensitivity" value="3"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY) == 3) {%>
						checked="checked" <%}%> /><%=speedMode[3]%>
				</div>

				<div class="unit">
					<label><%=rb
						.getString("bike_extend_iot_settings_upload_in_ride")%>：</label> <input
						type="radio" name="uploadInRide" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="uploadInRide" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="uploadInRide" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>

				<div class="unit">
					<label><%=rb
						.getString("bike_extend_iot_settings_upload_in_ride_interval")%>(S)：</label>
					<input type="number" name="uploadInterval"
						value="<%=extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE_INTERVAL)%>" />
				</div>

				<div class="unit">
					<label><%=rb
						.getString("bike_extend_iot_settings_heart_interval")%>(S)：</label> <input
						type="number" name="heartInterval"
						value="<%=extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_IOT_HEART_INTERVAL)%>" />
				</div>
			</c:if>

			<c:if test="${type == 'light' }">
				<div class="unit">
					<label><%=rb.getString("bike_extend_light_settings_head_light")%>：</label>
					<input type="radio" name="headLight" value="0"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="headLight" value="1"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="headLight" value="2"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
				<%-- <div class="unit">
					<label><%=rb
						.getString("bike_extend_light_settings_head_light_twinkling")%>：</label>
					<input type="radio" name="headLightTwinkling" value="0" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT_TWINKLING) == 0){ %>checked="checked"<%} %>/><%=settingsMode[0]%>
					<input type="radio" name="headLightTwinkling" value="1" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT_TWINKLING) == 1){ %>checked="checked"<%} %>/><%=settingsMode[1]%>
					<input type="radio" name="headLightTwinkling" value="2" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT_TWINKLING) == 2){ %>checked="checked"<%} %>/><%=settingsMode[2]%>
				</div> --%>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_light_settings_tail_light_twinkling")%>：</label> <input
						type="radio" name="tailLightTwinkling" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="tailLightTwinkling" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="tailLightTwinkling" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
				<div class="unit">
					<label><%=rb.getString("bike_extend_speed_mode")%>：</label> <input
						type="radio" value="0" name="speedMode"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE) == 0) {%>
						checked="checked" <%}%> /><%=speedMode[0]%>
					<input type="radio" value="1" name="speedMode"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE) == 1) {%>
						checked="checked" <%}%> /><%=speedMode[1]%>
					<input type="radio" value="2" name="speedMode"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE) == 2) {%>
						checked="checked" <%}%> /><%=speedMode[2]%>
					<input type="radio" value="3" name="speedMode"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE) == 3) {%>
						checked="checked" <%}%> /><%=speedMode[3]%>
				</div>

				<div class="unit">
					<label><%=rb
						.getString("bike_extend_light_settings_accelerator_response")%>：</label> <input
						type="radio" name="acceleratorResponse" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="acceleratorResponse" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="acceleratorResponse" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
			</c:if>

			<c:if test="${type == 'speed' }">
				<div class="unit">
					<label><%=rb.getString("bike_extend_speed_setings_inch_speed")%>：</label>
					<input type="radio" name="inchSpeed" value="0"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_INCH_SPEED) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="inchSpeed" value="1"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_INCH_SPEED) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="inchSpeed" value="2"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_INCH_SPEED) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_cruise_speed")%>：</label> <input
						type="radio" name="cruiseSpeed" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_CRUISE_SPEED) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="cruiseSpeed" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_CRUISE_SPEED) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="cruiseSpeed" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_CRUISE_SPEED) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
				<div class="unit">
					<label><%=rb.getString("bike_extend_speed_setings_start_type")%>：</label> <input
						type="radio" name="startType" value="0"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_START_TYPE) == 0) {%>
						checked="checked" <%}%> /><%=startType[0]%>
					<input type="radio" name="startType" value="1"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_START_TYPE) == 1) {%>
						checked="checked" <%}%> /><%=startType[1]%>
					<input type="radio" name="startType" value="2"
						<%if (extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_START_TYPE) == 2) {%>
						checked="checked" <%}%> /><%=startType[2]%>
				</div>
				<%-- <div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_ble_broadcast")%>：</label>
					<input type="radio" name="bleBroadcast" value="0" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_BLE_BROADCAST) == 0){ %>checked="checked"<%} %>/><%=settingsMode[0]%>
					<input type="radio" name="bleBroadcast" value="1" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_BLE_BROADCAST) == 1){ %>checked="checked"<%} %>/><%=settingsMode[1]%>
					<input type="radio" name="bleBroadcast" value="2" <%if(extendInfo.getIntValue(BikeVo.LOCK_EXTEND_INFO_BLE_BROADCAST) == 2){ %>checked="checked"<%} %>/><%=settingsMode[2]%>
				</div> --%>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_button_change_mode")%>：</label> <input
						type="radio" name="buttonChangeMode" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="buttonChangeMode" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="buttonChangeMode" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_button_open_head_light")%>：</label>
					<input type="radio" name="buttonChangeHeadLight" value="0"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 0) {%>
						checked="checked" <%}%> /><%=settingsMode[0]%>
					<input type="radio" name="buttonChangeHeadLight" value="1"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 1) {%>
						checked="checked" <%}%> /><%=settingsMode[1]%>
					<input type="radio" name="buttonChangeHeadLight" value="2"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT) == 2) {%>
						checked="checked" <%}%> /><%=settingsMode[2]%>
				</div>


				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_low_speed_limit")%>(6-25)：</label> <input
						type="number" name="lowSpeed" max="25"
						value="<%=extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_LOW_SPEED_LIMIT)%>" />
				</div>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_medium_speed_limit")%>(6-25)：</label>
					<input type="number" name="mediumSpeed" max="25"
						value="<%=extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_MEDIUM_SPEED_LIMIT)%>" />
				</div>
				<div class="unit">
					<label><%=rb
						.getString("bike_extend_speed_setings_high_speed_limit")%>(6-25)：</label>
					<input type="number" name="highSpeed" max="25"
						value="<%=extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_HIGH_SPEED_LIMIT)%>" />
				</div>
			</c:if>

			<c:if test="${type == 'key' }">
				<div class="unit">
					<label><%=rb.getString("bike_extend_key_settings_ble_key")%>：</label>
					<input type="text" name="bleKey" maxlength="8"
						value="<%=extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_KEY_BLE)%>" />
				</div>
			</c:if>

			<c:if test="${type == 'voice' }">
				<div class="unit">
					<label><%=rb.getString("common_type_title")%>：</label> <input
						type="radio" value="1" name="voiceType" /><%=voiceType[0]%>
					<input type="radio" value="2" name="voiceType" /><%=voiceType[1]%>
					<input type="radio" value="3" name="voiceType" /><%=voiceType[2]%>
				</div>
			</c:if>

			<c:if test="${type == 'address' }">
				<div class="unit">
					<label><%=rb.getString("bike_extend_adress_mode_title")%>：</label>
					<input type="radio" value="0" name="ipmode"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_ADDRESS_MODE) == 0) {%>
						checked="checked" <%}%> /><%=ipMode[0]%>
					<input type="radio" value="1" name="ipmode"
						<%if (extendInfo
						.getIntValue(BikeVo.LOCK_EXTEND_INFO_ADDRESS_MODE) == 0) {%>
						checked="checked" <%}%> /><%=ipMode[1]%>
				</div>
				<div class="unit">
					<label><%=rb.getString("bike_extend_adress_ip")%>：</label> <input
						type="text" name="ip" maxlength="20"
						value="<%= extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_ADDRESS_IP)%>" />
				</div>
				<div class="unit">
					<label><%=rb.getString("bike_extend_adress_port")%>：</label> <input
						type="text" name="port" maxlength="10"
						value="<%= extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_ADDRESS_PORT)%>" />
				</div>
			
			</c:if>
			
			
			<c:if test="${type == 'apn' }">
				<div class="unit">
					<label><%=rb.getString("bike_extend_adress_apn")%>：</label> <input
						type="text" name="apn" maxlength="20"
						value="<%= extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_ADDRESS_APN)%>" required="required"/>
				</div>
				<div class="unit">
					<label><%=rb.getString("login_username")%>：</label> <input
						type="text" name="user" maxlength="50"
						value="<%= extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_APN_USER)%>" />
				</div>
				<div class="unit">
					<label><%=rb.getString("login_password")%>：</label> <input
						type="text" name="password" maxlength="50"
						value="<%= extendInfo.getString(BikeVo.LOCK_EXTEND_INFO_APN_PASSWORD)%>" />
				</div>
				
			</c:if>
		</div>
		<%@ include file="../common/page_commit.jspf"%>
	</form>
</div>
