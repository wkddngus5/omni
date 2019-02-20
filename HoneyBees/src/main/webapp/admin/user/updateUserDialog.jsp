<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.pgt.bikelock.vo.UserVo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));

	String[] statusTitle = rb.getString("user_auth_status_value")
			.split(",");
	String[] genderValues = rb.getString("user_gender_value")
			.split(",");
	UserVo userDetailVo = (UserVo) request.getAttribute("userVo");
	int status = userDetailVo.getAuthStatus();
	if (status > 0) {
		status++;
	}

	String funcId = request.getAttribute("funcId").toString();
	String actionUrl = "userManage?requestType=30007&id="
			+ userDetailVo.getuId() + "&funcId=" + funcId;
%>

<div class="pageFormContent" layoutH="60">
	<form method="post" id="pageForm" action="<%=actionUrl%>"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<fieldset>
			<legend><%=rb.getString("user_base_info_title")%></legend>
			<dl>
				<dt><%=rb.getString("user_phone_title")%>：
				</dt>
				<dd>
					<input type="text" class="required digits" size="15"
						value="${userVo.phone}" name="phone" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("login_password")%>：
				</dt>
				<dd>
					<input type="text" size="15" name="password"/>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("nickname_title")%>：
				</dt>
				<dd>
					<input type="text" class="required" value="${userVo.nickName}"
						name="nickname" />
				</dd>
			</dl>

			<dl>
				<dt><%=rb.getString("user_money_title")%>：
				</dt>
				<dd>
					<input type="text" class="required" value="${userVo.money}"
						name="money" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_gift_money_title")%>：
				</dt>
				<dd>
					<input type="text" class="required" value="${userVo.giftMoney}"
						name="giftMoney" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_auth_status_title")%>：
				</dt>
				<dd>
					<select name="authStatus">
						<%
							for (int i = 1; i < statusTitle.length; i++) {
						%>
						<option value="<%=i - 1%>" <%if (i == status) {%>
							selected="selected" <%}%>><%=statusTitle[i]%></option>
						<%
							}
						%>
					</select>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_invait_code_title")%>：
				</dt>
				<dd>
					<input type="text" class="required" value="${userVo.invite_code}"
						name="invaitCode" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_credit_score_title")%>：
				</dt>
				<dd>
					<input type="text" class="required" value="${userVo.credit_score}"
						name="creditScore" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_area_title")%>：
				</dt>
				<dd>
					<select name="cityId">
						<option value="0"><%=rb.getString("common_all_title")%></option>
						<c:forEach items="${cityList}" varStatus="status" var="city">
							<option value="${city.id}"
								<c:if test="${city.id == tagCityId}">selected="selected"</c:if>><td>${city.name}</td>
							</option>
						</c:forEach>

					</select>
				</dd>
			</dl>

		</fieldset>


		<fieldset>
			<legend><%=rb.getString("user_detail_info_title")%></legend>
			<dl>
				<dt><%=rb.getString("user_firstname_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.firstname}"
						name="firstName" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_lastname_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.lastname}"
						name="lastName" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_gender_title")%>：
				</dt>
				<dd>
					<select name="gender">
						<%
							for (int i = 0; i < genderValues.length; i++) {
						%>
						<option value="<%=i%>"
							<%if (i == userDetailVo.getDetailVo().getGender()) {%>
							selected="selected" <%}%>><%=genderValues[i]%></option>
						<%
							}
						%>
					</select>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_birthday_title")%>：
				</dt>
				<dd>
					<input type="text" name="birthday" class="date"
						value="${userVo.detailVo.birthday}" dateFmt="yyyy-MM-dd HH:mm"
						readonly="true" /> <a class="inputDateButton" href="javascript:;">Select</a>
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_email_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.email}" name="email" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_address_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.address}"
						name="address" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_zipcode_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.zip_code}"
						name="zipCode" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_country_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.country}"
						name="counTry" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_redpack_amount")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.redpack}"
						name="redpack" />
				</dd>
			</dl>
			<dl>
				<dt><%=rb.getString("user_idcard_title")%>：
				</dt>
				<dd>
					<input type="text" value="${userVo.detailVo.idcard}" name="idcard" />
				</dd>
			</dl>
		</fieldset>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="submitForm()"><%=rb.getString("common_commit_title")%></button>
						</div>
					</div></li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close"><%=rb.getString("common_cancle_title")%></button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>
</div>

<script type="text/javascript">
		//提交
		function submitForm() {
			htmlObj = $.ajax({
				cache : true,
				type : "POST",
				url : "<%=actionUrl%>",
			data : $("#pageForm").serialize(),
			async : false
		});
		var obj = jQuery.parseJSON(htmlObj.responseText);
		if (obj.statusCode != 200) {
			alertMsg.error(obj.message, null);
		} else {
			navTab.reloadFlag("user_list");
			navTab.closeCurrentTab();
			alertMsg.correct(obj.data, null);
		}

	}
</script>
