<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.pgt.bikelock.vo.MembershipPlanVO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%

	// setup resources
	ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));

	String[] unitType = rb.getString("membership_plan_unit_value").split(",");
	pageContext.setAttribute("unitType", unitType);

	String[] rideUnitType = rb.getString("membership_plan_free_unit_value").split(",");
	pageContext.setAttribute("rideUnitType", rideUnitType);

	// instantiate MembershipPlanVO
	MembershipPlanVO plan = new MembershipPlanVO();
	if (request.getAttribute("plan") != null) {
		plan = (MembershipPlanVO) request.getAttribute("plan");
	}
	pageContext.setAttribute("plan", plan);

%>

<div class="pageContent">

	<form method="post" action="bikeManage?requestType=20057"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">

		<div class="pageFormContent" layoutH="58">

			<input type="hidden" name="id" value="${plan.id}" />

			<c:if test="${plan.id > 0}">

				<!-- Membership Title (can edit) -->
				<div class="unit">
					<label><%=rb.getString("common_title")%> ：</label> <input
						type="text" name="title" value="${plan.title}" size="30"
						class="required" />
				</div>

				<!-- Membership Description (can edit) -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_description")%> ：</label> <input
						type="text" name="description" value="${plan.description}"
						size="60" class="required" />
				</div>

				<!-- Membership Active -->
				<div class="unit">
					<label><%=rb.getString("common_status_title")%>：</label> <select
						name="active">


						<option value="1"
							<c:if test="${plan.active}">
								selected="selected"
							</c:if>>
							Active</option>

						<option value="0"
							<c:if test="${!plan.active}">
								selected="selected"
							</c:if>>
							Canceled</option>

					</select>
				</div>

				<!-- Membership Education -->
				<div class="unit">
					<label>Is Education ：</label> <select name="education">

						<option value="1"
							<c:if test="${plan.education}">
								selected="selected"
							</c:if>>
							Education</option>

						<option value="0"
							<c:if test="${!plan.education}">
								selected="selected"
							</c:if>>
							Not Education</option>

					</select>
				</div>
			</c:if>
			<c:if test="${empty plan.id}">

				<!-- Membership Title -->
				<div class="unit">
					<label><%=rb.getString("common_title")%> ：</label> <input
						type="text" name="title" value="${plan.title}" size="30"
						class="required" />
				</div>

				<!-- Membership Description -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_description")%> ：</label> <input
						type="text" name="description" value="${plan.description}"
						size="60" class="required" />
				</div>

				<!-- Membership Unit  -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_unit")%>：</label> <select
						name="interval">


						<c:forEach items="${unitType}" varStatus="status"
							var="membershipUnit">
							<c:if test="${not empty membershipUnit}">

								<option value="${status.index}"
									<c:if test="${status.index eq plan.interval}">
										selected="selected"
									</c:if>>
									${membershipUnit}</option>

							</c:if>
						</c:forEach>

					</select>
				</div>

				<!-- Membership Count  -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_unit_count")%> ：</label> <input
						type="text" name="intervalCount" value="${plan.intervalCount}"
						size="30" class="required" />
				</div>

				<!-- Membership Price -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_unit_price")%>：</label> <input
						type="text" name="planPrice" value="${plan.planPrice}" size="30"
						class="required" />
				</div>

				<!-- Membership Free Ride Unit -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_free_unit")%>：</label> <select
						name="rideUnit">


						<c:forEach items="${rideUnitType}" varStatus="status"
							var="rideUnit">
							<c:if test="${not empty rideUnit}">

								<option value="${status.index}"
									<c:if test="${status.index eq plan.interval}">
										selected="selected"
									</c:if>>
									${rideUnit}</option>

							</c:if>
						</c:forEach>

					</select>
				</div>

				<!-- Membership Free Count  -->
				<div class="unit">
					<label><%=rb.getString("membership_plan_free_unit_count")%>
						：</label> <input type="text" name="rideFreeUnitCount"
						value="${plan.rideFreeUnitCount}" size="30" class="required" />
				</div>

				<!-- Membership Renewable -->
				<div class="unit">
					<label>Is Renewable ：</label> <select name="isRenewable">

						<option value="1"
							<c:if test="${plan.isRenewable}">
								selected="selected"
							</c:if>>
							Renewable</option>

						<option value="0"
							<c:if test="not ${plan.isRenewable}">
								selected="selected"
							</c:if>>
							Not Renewable</option>

					</select>
				</div>
				<!-- Membership Education -->
				<div class="unit">
					<label>Is Education ：</label> <select name="education">

						<option value="1"
							<c:if test="${plan.education}">
								selected="selected"
							</c:if>>
							Education</option>

						<option value="0"
							<c:if test="not ${plan.education}">
								selected="selected"
							</c:if>>
							Not Education</option>

					</select>
				</div>

				<!-- Membership City and Admin Check -->
				<%@ include file="../common/page_city.jspf"%>
				<%@ include file="../common/page_admin_check.jspf"%>

			</c:if>

		</div>

		<%@ include file="../common/page_commit.jspf"%>

	</form>
</div>
