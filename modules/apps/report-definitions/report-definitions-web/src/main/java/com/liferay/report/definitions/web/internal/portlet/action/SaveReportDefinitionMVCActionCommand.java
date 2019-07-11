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

package com.liferay.report.definitions.web.internal.portlet.action;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.data.engine.rest.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.dto.v1_0.DataLayout;
import com.liferay.data.engine.rest.resource.v1_0.DataDefinitionResource;
import com.liferay.data.engine.rest.resource.v1_0.DataLayoutResource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseTransactionalMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.report.definitions.model.ReportDefinition;
import com.liferay.report.definitions.service.ReportDefinitionLocalService;
import com.liferay.report.definitions.web.internal.constants.ReportDefinitionPortletKeys;

import java.util.HashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ReportDefinitionPortletKeys.PORTLET_NAME,
		"mvc.command.name=saveReportDefinition"
	},
	service = MVCActionCommand.class
)
public class SaveReportDefinitionMVCActionCommand
	extends BaseTransactionalMVCActionCommand {

	@Override
	protected void doTransactionalCommand(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		LiferayPortletURL portletURL = PortletURLFactoryUtil.create(
			actionRequest, themeDisplay.getPpid(), PortletRequest.RENDER_PHASE);

		String mvcRenderCommandName = ParamUtil.getString(
			actionRequest, "mvcRenderCommandName");

		portletURL.setParameter("mvcRenderCommandName", mvcRenderCommandName);

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		try {
			ReportDefinition reportDefinition = _saveReportDefinition(
				actionRequest);

			portletURL.setParameter(
				"reportDefinitionId",
				String.valueOf(reportDefinition.getReportDefinitionId()));

			portletURL.setParameter("redirect", redirect);

			actionRequest.setAttribute(WebKeys.REDIRECT, portletURL.toString());
		}
		catch (PortalException pe) {
			SessionErrors.add(actionRequest, pe.getClass(), pe);
		}
	}

	private ReportDefinition _addReportDefinition(ActionRequest actionRequest)
		throws Exception {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			ReportDefinition.class.getName(), actionRequest);

		DataDefinition dataDefinition = _saveDataDefinition(
			actionRequest, ParamUtil.getLong(actionRequest, "dataDefinitionId"),
			ParamUtil.getString(actionRequest, "dataDefinition"),
			ParamUtil.getString(actionRequest, "description"),
			serviceContext.getScopeGroupId(), serviceContext.getLanguageId(),
			ParamUtil.getString(actionRequest, "name"));

		_saveDataLayout(
			actionRequest, dataDefinition,
			ParamUtil.getLong(actionRequest, "dataLayoutId"),
			ParamUtil.getString(actionRequest, "dataLayout"),
			ParamUtil.getString(actionRequest, "description"),
			serviceContext.getLanguageId(),
			ParamUtil.getString(actionRequest, "name"));

		return _reportDefinitionLocalService.addReportDefinition(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			dataDefinition.getId(), ParamUtil.getString(actionRequest, "name"),
			ParamUtil.getString(actionRequest, "description"),
			ParamUtil.getString(actionRequest, "availableColumns"),
			ParamUtil.getString(actionRequest, "sortColumns"), serviceContext);
	}

	private DataDefinition _saveDataDefinition(
			ActionRequest actionRequest, long dataDefinitionId,
			String dataDefinitionJSON, String description, long groupId,
			String languageId, String name)
		throws Exception {

		//DataDefinition dataDefinition = DataDefinitionSerDes.toDTO(
		//	dataDefinitionJSON);

		DataDefinition dataDefinition = _objectMapper.readValue(
			dataDefinitionJSON, DataDefinition.class);

		dataDefinition.setId(dataDefinitionId);

		if (Validator.isNotNull(description)) {
			dataDefinition.setDescription(
				new HashMap() {
					{
						put(languageId, description);
					}
				});
		}

		if (Validator.isNotNull(name)) {
			dataDefinition.setName(
				new HashMap() {
					{
						put(languageId, name);
					}
				});
		}

		_dataDefinitionResource.setContextCompany(
			_portal.getCompany(actionRequest));

		if (dataDefinitionId > 0) {
			_dataDefinitionResource.putDataDefinition(
				dataDefinitionId, dataDefinition);

			return dataDefinition;
		}

		return _dataDefinitionResource.postSiteDataDefinition(
			groupId, dataDefinition);
	}

	private void _saveDataLayout(
			ActionRequest actionRequest, DataDefinition dataDefinition,
			long dataLayoutId, String dataLayoutJSON, String description,
			String languageId, String name)
		throws Exception {

		//DataLayout dataLayout = DataLayoutSerDes.toDTO(dataLayoutJSON);

		DataLayout dataLayout = _objectMapper.readValue(
			dataLayoutJSON, DataLayout.class);

		dataLayout.setId(dataLayoutId);
		dataLayout.setDataDefinitionId(dataDefinition.getId());
		dataLayout.setDataLayoutKey(dataDefinition.getDataDefinitionKey());

		if (Validator.isNotNull(description)) {
			dataLayout.setDescription(
				new HashMap() {
					{
						put(languageId, description);
					}
				});
		}

		if (Validator.isNotNull(name)) {
			dataLayout.setName(
				new HashMap() {
					{
						put(languageId, name);
					}
				});
		}

		_dataLayoutResource.setContextCompany(
			_portal.getCompany(actionRequest));

		if (dataLayoutId > 0) {
			_dataLayoutResource.putDataLayout(dataLayoutId, dataLayout);
		}
		else {
			_dataLayoutResource.postDataDefinitionDataLayout(
				dataDefinition.getId(), dataLayout);
		}
	}

	private ReportDefinition _saveReportDefinition(ActionRequest actionRequest)
		throws Exception {

		long reportDefinitionId = ParamUtil.getLong(
			actionRequest, "reportDefinitionId");

		if (reportDefinitionId == 0) {
			return _addReportDefinition(actionRequest);
		}

		return _updateReportDefinition(actionRequest, reportDefinitionId);
	}

	private ReportDefinition _updateReportDefinition(
			ActionRequest actionRequest, long reportDefinitionId)
		throws Exception {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			ReportDefinition.class.getName(), actionRequest);

		String dataDefinitionJSON = ParamUtil.getString(
			actionRequest, "dataDefinition");

		if (Validator.isNotNull(dataDefinitionJSON)) {
			DataDefinition dataDefinition = _saveDataDefinition(
				actionRequest,
				ParamUtil.getLong(actionRequest, "dataDefinitionId"),
				dataDefinitionJSON,
				ParamUtil.getString(actionRequest, "description"),
				serviceContext.getScopeGroupId(),
				serviceContext.getLanguageId(),
				ParamUtil.getString(actionRequest, "name"));

			_saveDataLayout(
				actionRequest, dataDefinition,
				ParamUtil.getLong(actionRequest, "dataLayoutId"),
				ParamUtil.getString(actionRequest, "dataLayout"),
				ParamUtil.getString(actionRequest, "description"),
				serviceContext.getLanguageId(),
				ParamUtil.getString(actionRequest, "name"));
		}

		return _reportDefinitionLocalService.updateReportDefinition(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			reportDefinitionId,
			ParamUtil.getLong(actionRequest, "dataDefinitionId"),
			ParamUtil.getString(actionRequest, "name"),
			ParamUtil.getString(actionRequest, "description"),
			ParamUtil.getString(actionRequest, "availableColumns"),
			ParamUtil.getString(actionRequest, "sortColumns"), serviceContext);
	}

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
			setDateFormat(new ISO8601DateFormat());
			//setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			//setSerializationInclusion(JsonInclude.Include.NON_NULL);
			setVisibility(
				PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			setVisibility(
				PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
		}
	};

	@Reference
	private DataDefinitionResource _dataDefinitionResource;

	@Reference
	private DataLayoutResource _dataLayoutResource;

	@Reference
	private Portal _portal;

	@Reference
	private ReportDefinitionLocalService _reportDefinitionLocalService;

}