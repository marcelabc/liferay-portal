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
String redirect = ParamUtil.getString(request, "redirect");
ReportDefinition reportDefinition = displayContext.getReportDefinition();

long reportDefinitionId = BeanParamUtil.getLong(reportDefinition, request, "reportDefinitionId");
long dataDefinitionId = displayContext.getDataDefinitionId();
long dataLayoutId = displayContext.getDataLayoutId();
%>

<portlet:actionURL name="saveReportDefinition" var="saveReportDefinitionURL">
	<portlet:param name="mvcRenderCommandName" value="/edit_report_definition" />
</portlet:actionURL>

<aui:form action="<%= saveReportDefinitionURL %>" method="post" name="fm">
	<aui:input name="reportDefinitionId" type="hidden" value="<%= reportDefinitionId %>" />
	<aui:input name="dataDefinition" type="hidden" />
	<aui:input name="dataDefinitionId" type="hidden" value="<%= dataDefinitionId %>" />
	<aui:input name="dataLayout" type="hidden" />
	<aui:input name="dataLayoutId" type="hidden" value="<%= dataLayoutId %>" />

	<div class="container-fluid-1280 report-definitions-form">
		<div class="lfr-form-content">
			<aui:fieldset-group markupView="lexicon">
				<aui:fieldset collapsible="<%= true %>" label="name-and-description">
					<aui:input label="name" name="name" type="text" value="<%= HtmlUtil.escape(displayContext.getReportDefinitionName()) %>" />

					<aui:input label="description" name="description" type="textarea" value="<%= HtmlUtil.escape(displayContext.getReportDefinitionDescription()) %>" />
				</aui:fieldset>

				<aui:fieldset collapsible="<%= true %>" label="database-columns">
					<aui:input label="available-columns" name="availableColumns" type="textarea" value="<%= HtmlUtil.escape(displayContext.getReportDefinitionAvailableColumns()) %>" />
					<aui:input label="sort-columns" name="sortColumns" type="textarea" value="<%= HtmlUtil.escape(displayContext.getReportDefinitionSortColumns()) %>" />
				</aui:fieldset>

				<aui:fieldset collapsed="<%= true %>" collapsible="<%= true %>" label="parameters">
					<liferay-data-engine:data-layout-builder
						dataDefinitionInputId="dataDefinition"
						dataLayoutId="<%= dataLayoutId %>"
						dataLayoutInputId="dataLayout"
						namespace="<%= renderResponse.getNamespace() %>"
					>
						<button class="btn btn-primary lfr-ddm-add-field lfr-ddm-plus-button nav-btn nav-btn-monospaced" id="addFieldButton" type="button">
							<svg class="lexicon-icon">
								<use xlink:href="<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg#plus" />
							</svg>
						</button>
					</liferay-data-engine:data-layout-builder>
				</aui:fieldset>

				<aui:fieldset collapsed="<%= true %>" collapsible="<%= true %>" label="permissions">
					<liferay-ui:input-permissions
						modelName="<%= ReportDefinition.class.getName() %>"
					/>
				</aui:fieldset>
			</aui:fieldset-group>
		</div>

		<aui:button-row>
			<aui:button type="submit" />

			<aui:button href="<%= redirect %>" type="cancel" />
		</aui:button-row>
	</div>
</aui:form>