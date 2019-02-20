<%@page import="com.pgt.bikelock.utils.ValueUtil"%>
<%@page import="com.pgt.bikelock.resource.OthersSource"%>
<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] showItem = {};
	if (OthersSource.getSourceString("user_detail_show_info") != null) {
		showItem = OthersSource
				.getSourceString("user_detail_show_info").split(",");
	}

%>

<div class="pageFormContent" layoutH="60">
	<fieldset>
		<legend><%=rb.getString("user_base_info_title")%>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<c:if test="${admin == 1}">
		<a style='color:green;' target="navTab" href="userManage?requestType=30007&funcId=147&id=${userVo.uId}"><%=rb.getString("common_update_title")%></a>
		</c:if>
		</legend>

		<%
			if (ValueUtil.checkItemInArr(showItem, "userId")) {
		%>
		<dl>
			<dt><%=rb.getString("user_id_title")%>：
			</dt>
			<dd>${userVo.uId}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "phone")) {
		%>
		<dl>
			<dt><%=rb.getString("user_phone_title")%>：
			</dt>
			<dd>${userVo.phone}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "nickname")) {
		%>
		<dl>
			<dt><%=rb.getString("nickname_title")%>：
			</dt>
			<dd>${userVo.nickName}</dd>
		</dl>
		<%
			}
		%>


		<%
			if (ValueUtil.checkItemInArr(showItem, "headimage")) {
		%>
		<dl style="height: 50;">
			<dt><%=rb.getString("user_head_image_title")%>：
			</dt>
			<dd>
				<img src="${userVo.headUrl}" width="50" height="50"/>
			</dd>
		</dl>
		<%
			}
		%>



		<%
			if (ValueUtil.checkItemInArr(showItem, "balance")) {
		%>
		<dl>
			<dt><%=rb.getString("user_money_title")%>：
			</dt>
			<dd>${userVo.money}</dd>
		</dl>
		<%
			}
		%>

	<%
			if (ValueUtil.checkItemInArr(showItem, "gift_balance")) {
		%>
		<dl>
			<dt><%=rb.getString("user_gift_money_title")%>：
			</dt>
			<dd>${userVo.giftMoney}</dd>
		</dl>
		<%
			}
		%>



		<%
			if (ValueUtil.checkItemInArr(showItem, "auth_status")) {
		%>
		<dl>
			<dt><%=rb.getString("user_auth_status_title")%>：
			</dt>
			<dd>${userVo.authStatusStr}</dd>
		</dl>
		<%
			}
		%>



		<%
			if (ValueUtil.checkItemInArr(showItem, "invitation_code")) {
		%>
		<dl>
			<dt><%=rb.getString("user_invait_code_title")%>：
			</dt>
			<dd>${userVo.invite_code}</dd>
		</dl>
		<%
			}
		%>


		<%
			if (ValueUtil.checkItemInArr(showItem, "credit_score")) {
		%>
		<dl>
			<dt><%=rb.getString("user_credit_score_title")%>：
			</dt>
			<dd>${userVo.credit_score}</dd>
		</dl>
		<%
			}
		%>


		<%
			if (ValueUtil.checkItemInArr(showItem, "area")) {
		%>
		<dl>
			<dt><%=rb.getString("user_area_title")%>：
			</dt>
			<dd>${userVo.cityName}</dd>
		</dl>
		<%
			}
		%>


		<%
			if (ValueUtil.checkItemInArr(showItem, "register_date")) {
		%>
		<dl>
			<dt><%=rb.getString("user_register_date_title")%>：
			</dt>
			<dd>${userVo.register_date}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "login_date")) {
		%>
		<dl>
			<dt><%=rb.getString("user_login_date_title")%>：
			</dt>
			<dd>${userVo.login_date}</dd>
		</dl>
		<%
			}
		%>

	</fieldset>


	<fieldset>
		<legend><%=rb.getString("user_detail_info_title")%></legend>
		<%
			if (ValueUtil.checkItemInArr(showItem, "first_name")) {
		%>
		<dl>
			<dt><%=rb.getString("user_firstname_title")%>：
			</dt>
			<dd>${userVo.detailVo.firstname}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "last_name")) {
		%>
		<dl>
			<dt><%=rb.getString("user_lastname_title")%>：
			</dt>
			<dd>${userVo.detailVo.lastname}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "gender")) {
		%>
		<dl>
			<dt><%=rb.getString("user_gender_title")%>：
			</dt>
			<dd>${userVo.detailVo.genderStr}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "birthday")) {
		%>
		<dl>
			<dt><%=rb.getString("user_birthday_title")%>：
			</dt>
			<dd>${userVo.detailVo.birthday}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "email")) {
		%>
		<dl>
			<dt><%=rb.getString("user_email_title")%>：
			</dt>
			<dd>${userVo.detailVo.email}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "address")) {
		%>
		<dl>
			<dt><%=rb.getString("user_address_title")%>：
			</dt>
			<dd>${userVo.detailVo.address}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "zipcode")) {
		%>
		<dl>
			<dt><%=rb.getString("user_zipcode_title")%>：
			</dt>
			<dd>${userVo.detailVo.zip_code}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "country")) {
		%>
		<dl>
			<dt><%=rb.getString("user_country_title")%>：
			</dt>
			<dd>${userVo.detailVo.country}</dd>
		</dl>
		<%
			}
		%>


		<%
			if (ValueUtil.checkItemInArr(showItem, "redpack_amount")) {
		%>
		<dl>
			<dt><%=rb.getString("user_redpack_amount")%>：
			</dt>
			<dd>${userVo.detailVo.redpack}</dd>
		</dl>
		<%
			}
		%>

		<%
			if (ValueUtil.checkItemInArr(showItem, "idcard")) {
		%>
		<dl>
			<dt><%=rb.getString("user_idcard_title")%>：
			</dt>
			<dd>${userVo.detailVo.idcard}</dd>
		</dl>
		<%
			}
		%>

	</fieldset>

	<fieldset>
		<legend><%=rb.getString("user_riding_info")%></legend>
		<dl>
			<dt>${distanceUnit}：
			</dt>
			<dd>${distance}</dd>
		</dl>


		<dl>
			<dt><%=rb.getString("user_riding_info_duration")%>：
			</dt>
			<dd>${useInfo.duration}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("user_riding_info_amount")%>：
			</dt>
			<dd>${useInfo.amount}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("user_riding_info_calorie")%>：
			</dt>
			<dd>${useInfo.calorie}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("user_riding_info_carbon")%>：
			</dt>
			<dd>${useInfo.carbon}</dd>
		</dl>

		<dl>
			<dt><%=rb.getString("user_riding_info_active_day")%>：
			</dt>
			<dd>${useInfo.activeDay}</dd>
		</dl>
	</fieldset>
</div>
