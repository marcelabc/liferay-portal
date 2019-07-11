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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

ReportDefinition reportDefinition = (ReportDefinition)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcRenderCommandName" value="/edit_report_definition" />
		<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
		<portlet:param name="reportDefinitionId" value="<%= String.valueOf(reportDefinition.getReportDefinitionId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		message="edit"
		url="<%= editURL %>"
	/>

	<portlet:actionURL name="deleteReportDefinition" var="deleteURL">
		<portlet:param name="reportDefinitionId" value="<%= String.valueOf(reportDefinition.getReportDefinitionId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		url="<%= deleteURL %>"
	/>

	<portlet:renderURL var="executeURL">
		<portlet:param name="mvcRenderCommandName" value="/view_report_definition" />
		<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
		<portlet:param name="reportDefinitionId" value="<%= String.valueOf(reportDefinition.getReportDefinitionId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		message="execute"
		url="<%= executeURL %>"
	/>
</liferay-ui:icon-menu>