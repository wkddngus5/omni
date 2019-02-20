<%@page import="com.pgt.bikelock.vo.BikeVo"%>
<%@page import="com.pgt.bikelock.utils.ValueUtil"%>
<%@page import="com.pgt.bikelock.resource.OthersSource"%>
<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" import="com.pgt.bikelock.vo.BikeVo"%>

<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] connectStatusValue = rb.getString(
			"bike_connect_status_value").split(",");
	String[] speedModeValue = rb.getString("bike_extend_mode_values")
			.split(",");
	String funcId = "168";// request.getAttribute("funcId").toString();
	String extend = (String) request.getAttribute("extendInfo");
	JSONObject extendInfo = extend != null ? JSONObject
			.parseObject(extend) : null;
%>
<div class="pageFormContent" layoutH="60">
	<fieldset>
		<legend><%=rb.getString("user_base_info_title")%>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:if test="${admin == 1}">
			<a style='color:green;' target="dialog"
				href="bikeManage?requestType=20029&id=${bikeVo.bid}"><%=rb.getString("common_update_title")%></a>
		</c:if>
		</legend>
		<dl>
			<dt>ID：</dt>
			<dd>${bikeVo.bid}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_id_title")%>：
			</dt>
			<dd>${bikeVo.number}</dd>
		</dl>
		<dl>
			<dt>IMEI：</dt>
			<dd>${bikeVo.imei}</dd>
		</dl>
		<dl>
			<dt>MAC：</dt>
			<dd>${bikeVo.mac}</dd>
		</dl>
		<dl>
			<dt>ICCID：</dt>
			<dd>${simVo.iccid}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_gps_time")%>：
			</dt>
			<dd>${bikeVo.gTime}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_gps_lng")%>：
			</dt>
			<dd>${bikeVo.gLng}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_gps_lat")%>：
			</dt>
			<dd>${bikeVo.gLat}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_show_in_map_title")%>：
			</dt>
			<dd>
				<a href="bikeManage?requestType=20020&id=${bikeVo.bid}"
					target="_blank"><img src="${mapIcon}" width="20" height="20" /></a>
			</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_power")%>：
			</dt>
			<dd>${bikeVo.powerPercent}%(${bikeVo.power})</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_gsm")%>：
			</dt>
			<dd>${bikeVo.gsm}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_heart_time")%>：
			</dt>
			<dd>${bikeVo.heartTime}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_version_title")%>：
			</dt>
			<dd>${bikeVo.version}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_version_time")%>：
			</dt>
			<dd>${bikeVo.versionTime}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_status_title")%>：
			</dt>
			<dd>${bikeVo.statusStr}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("common_add_date_title")%>：
			</dt>
			<dd>${bikeVo.add_date}</dd>
		</dl>
		<dl>
			<dt>Server IP：</dt>
			<dd>${bikeVo.serverIp}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_lock_type_title")%>：</dt>
			<dd>${bikeVo.bikeTypeStr}</dd>
		</dl>
	</fieldset>

	<c:if test="${bikeVo.bikeType == 2 }">
		<fieldset>
			<legend><%=rb.getString("bike_extend_info_title")%>&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if test="${admin == 1}">
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=voice&imei=${bikeVo.imei}"><%=rb.getString("bike_lock_voice")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=IOT&imei=${bikeVo.imei}"><%=rb.getString("bike_extend_iot_settings")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=light&imei=${bikeVo.imei}"><%=rb.getString("bike_extend_light_settings")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=speed&imei=${bikeVo.imei}"><%=rb.getString("bike_extend_speed_settings")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=key&imei=${bikeVo.imei}"><%=rb.getString("bike_extend_key_settings")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=address&imei=${bikeVo.imei}"><%=rb.getString("bike_extend_adress")%></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="dialog" href="bikeManage?requestType=20102&funcId=<%=funcId%>&type=apn&imei=${bikeVo.imei}">APN</a>&nbsp;&nbsp;&nbsp;&nbsp;
				
				<a target="dialog" width="750" height="300" href="bikeManage?requestType=20103&funcId=<%=funcId%>&imei=${bikeVo.imei}" >Upgrade</a>&nbsp;&nbsp;&nbsp;&nbsp;

				<%-- <a href="#" onclick="upgrade(${bikeVo.imei},2)">Upgrade IOT</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" onclick="upgrade(${bikeVo.imei},3)">Upgrade CTL</a>&nbsp;&nbsp;&nbsp;&nbsp; --%>
			</c:if>
			</legend>
			<%
				if (extendInfo != null) {
			%>
			<!-- info1 -->
			<dl>
				<dt><%=rb.getString("bike_extend_speed")%>：
				</dt>
				<dd>
					<%=extendInfo.get(BikeVo.LOCK_EXTEND_INFO_SPEED)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_speed_mode")%>：
				</dt>
				<dd>
					<%=speedModeValue[extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE)]%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_total_mileage")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_TOTAL_MILEAGE)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_prescient_mileage")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_PRESCIENT_MILEAGE)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_total_time")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_TOTAL_TIME)%>
				</dd>
			</dl>

			<!-- info2 -->
			<dl>
				<dt><%=rb.getString("bike_extend_charge_status")%>(M)：
				</dt>
				<dd>
					<%
						if (extendInfo
										.getIntValue(BikeVo.LOCK_EXTEND_INFO_CHARGE_STATUS) == 1) {
					%>
					<%=rb.getString("common_yes_title")%>
					<%
						} else {
					%>
					<%=rb.getString("common_no_title")%>
					<%
						}
					%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_voltage")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_VOLTAGE)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_cycles")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_CYCLES)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_power1")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_POWER1)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_power2")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_POWER2)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_builtin_temp1")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUILTIN_TEMP1)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_builtin_temp2")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_BUILTIN_TEMP2)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_plugin_temp1")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_PLUGIN_TEMP1)%>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("bike_extend_plugin_temp2")%>(M)：
				</dt>
				<dd>
					<%=extendInfo
							.getIntValue(BikeVo.LOCK_EXTEND_INFO_PLUGIN_TEMP2)%>
				</dd>
			</dl>
			<%
				}
			%>
		</fieldset>
	</c:if>

	<fieldset>
		<legend><%=rb.getString("bike_status_tips_title")%></legend>
		<dl>
			<dt><%=rb.getString("bike_connect_status_title")%>：
			</dt>
			<dd>
				<c:if test="${connect == 1}"><%=connectStatusValue[0]%></c:if>
				<c:if test="${connect == 0}"><%=connectStatusValue[1]%></c:if>
			</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_use_count")%>：
			</dt>
			<dd>${bikeVo.rideCount}</dd>
		</dl>
		<dl>
			<dt><%=rb.getString("bike_lock_error_title")%>：
			</dt>
			<dd>${bikeVo.errorStr}</dd>
		</dl>
		<c:if test="${reportContent != null}">
			<dl>
				<dt><%=rb.getString("bike_manage_report")%>：
				</dt>
				<dd>${reportContent}</dd>
			</dl>
		</c:if>

	</fieldset>
</div>

<script type="text/javascript">
	function upgrade(imei,versionType) {
		var defaultType = '8A'
		if(versionType == 3){
			defaultType = '20';
		}
		var lockType = window.prompt("确认升级？请输入滑板车类型",defaultType);
		if (lockType != "" && lockType != null) {
			htmlObj = $.ajax({
				cache : true,
				type : "POST",
				url : "bikeManage?requestType=20103&imei=" + imei + "&versionType="
						+ versionType+'&lockType='+lockType,
				data : $("#pageForm").serialize(),
				async : false
			});
			var obj = jQuery.parseJSON(htmlObj.responseText);
			
			if (obj.statusCode != 200) {
				alertMsg.error(obj.message, null);
			}
			alert(obj.data);
		}

	}
</script>
