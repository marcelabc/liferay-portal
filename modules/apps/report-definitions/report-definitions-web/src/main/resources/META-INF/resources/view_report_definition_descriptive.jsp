<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
String displayStyle = displayContext.getDisplayStyle();
PortletURL portletURL = displayContext.getPortletURL();
%>

<liferay-util:include page="/navigation_bar.jsp" servletContext="<%= application %>" />

<liferay-util:include page="/management_bar.jsp" servletContext="<%= application %>" />

<div class="container-fluid-1280" id="<portlet:namespace />formContainer">
	<aui:form action="<%= portletURL.toString() %>" method="post" name="searchContainerForm">
		<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
		<aui:input name="deleteReportDefinitionIds" type="hidden" />

		<liferay-ui:search-container
			id="<%= displayContext.getSearchContainerId() %>"
			searchContainer="<%= displayContext.getSearch() %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.report.definitions.model.ReportDefinition"
				cssClass="entry-display-style"
				keyProperty="reportDefinitionId"
				modelVar="reportDefinition"
			>
				<portlet:renderURL var="rowURL">
					<portlet:param name="mvcRenderCommandName" value="/edit_report_definition" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="reportDefinitionId" value="<%= String.valueOf(reportDefinition.getReportDefinitionId()) %>" />
					<portlet:param name="displayStyle" value="<%= displayStyle %>" />
				</portlet:renderURL>

				<c:choose>
					<c:when test='<%= displayStyle.equals("a") %>'>
						<liferay-ui:search-container-column-icon
							cssClass="asset-icon"
							icon="forms"
						/>

						<liferay-ui:search-container-column-jsp
							colspan="<%= 2 %>"
							href="<%= rowURL %>"
							path="/view_report_definition_descriptive.jsp"
						/>

						<liferay-ui:search-container-column-jsp
							path="/report_definition_action.jsp"
						/>
					</c:when>
					<c:otherwise>
						<liferay-ui:search-container-column-text
							cssClass="table-cell-expand table-title"
							href="<%= rowURL %>"
							name="name"
							value="<%= HtmlUtil.escape(reportDefinition.getName()) %>"
						/>

						<liferay-ui:search-container-column-text
							cssClass="table-cell-expand"
							name="description"
							value="<%= HtmlUtil.escape(reportDefinition.getDescription()) %>"
						/>

						<liferay-ui:search-container-column-date
							cssClass="table-cell-expand-smaller"
							name="modified-date"
							value="<%= reportDefinition.getModifiedDate() %>"
						/>

						<liferay-ui:search-container-column-jsp
							path="/report_definition_action.jsp"
						/>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= displayStyle %>"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</div>