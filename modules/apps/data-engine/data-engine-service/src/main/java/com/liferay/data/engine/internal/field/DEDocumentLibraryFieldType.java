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

package com.liferay.data.engine.internal.field;

import com.liferay.data.engine.exception.DEDataDefinitionDeserializerException;
import com.liferay.data.engine.exception.DEDataDefinitionSerializerException;
import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.AggregateResourceBundle;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcela Cunha
 */
@Component(
	immediate = true,
	property = "de.data.definition.field.type=document_library",
	service = DEFieldType.class
)
public class DEDocumentLibraryFieldType implements DEFieldType {

	@Override
	public DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinitionField deDataDefinitionField =
			DEFieldType.super.deserialize(jsonObject);

		if (jsonObject.has("groupId")) {
			deDataDefinitionField.setCustomProperty(
				"groupId", jsonObject.getLong("groupId"));
		}

		if (jsonObject.has("itemSelectorAuthToken")) {
			deDataDefinitionField.setCustomProperty(
				"itemSelectorAuthToken",
				jsonObject.getString("itemSelectorAuthToken"));
		}

		if (jsonObject.has("lexiconIconsPath")) {
			deDataDefinitionField.setCustomProperty(
				"lexiconIconsPath", jsonObject.getString("lexiconIconsPath"));
		}

		if (jsonObject.has("select")) {
			deDataDefinitionField.setCustomProperty(
				"select", jsonObject.getString("select"));
		}

		return deDataDefinitionField;
	}

	@Override
	public void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		DEFieldType.super.includeContext(
			httpServletRequest, httpServletResponse, context,
			deDataDefinitionField, readOnly);

		if (deDataDefinitionField.hasCustomProperty("readOnly") &&
			Validator.isNotNull(
				deDataDefinitionField.hasCustomProperty("value"))) {

			JSONObject valueJSONObject = getValueJSONObject(
				(String)deDataDefinitionField.getCustomProperty("value"));

			if ((valueJSONObject != null) && (valueJSONObject.length() > 0)) {
				FileEntry fileEntry = getFileEntry(valueJSONObject);

				context.put("fileEntryTitle", getFileEntryTitle(fileEntry));
				context.put(
					"fileEntryURL",
					getFileEntryURL(httpServletRequest, fileEntry));
			}
		}

		if (deDataDefinitionField.hasCustomProperty("groupId")) {
			context.put(
				"groupId", deDataDefinitionField.getCustomProperty("groupId"));
		}

		context.put(
			"itemSelectorAuthToken",
			getItemSelectorAuthToken(httpServletRequest));

		context.put(
			"lexiconIconsPath", getLexiconIconsPath(httpServletRequest));

		Map<String, String> stringsMap = new HashMap<>();

		Locale displayLocale = getDisplayLocale(httpServletRequest);

		ResourceBundle resourceBundle = getResourceBundle(displayLocale);

		stringsMap.put("select", LanguageUtil.get(resourceBundle, "select"));

		context.put("strings", stringsMap);

		String value = (String)deDataDefinitionField.getCustomProperty("value");

		if (Validator.isNull(value)) {
			value = "{}";
		}

		context.put("value", jsonFactory.looseDeserialize(value));
	}

	@Override
	public JSONObject serialize(
			DEDataDefinitionField deDataDefinitionField,
			JSONFactory jsonFactory)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = DEFieldType.super.serialize(
			deDataDefinitionField, jsonFactory);

		if (deDataDefinitionField.hasCustomProperty("groupId")) {
			jsonObject.put(
				"groupId", deDataDefinitionField.getCustomProperty("groupId"));
		}

		if (deDataDefinitionField.hasCustomProperty("itemSelectorAuthToken")) {
			jsonObject.put(
				"itemSelectorAuthToken",
				deDataDefinitionField.getCustomProperty(
					"itemSelectorAuthToken"));
		}

		if (deDataDefinitionField.hasCustomProperty("lexiconIconsPath")) {
			jsonObject.put(
				"lexiconIconsPath",
				deDataDefinitionField.getCustomProperty("lexiconIconsPath"));
		}

		if (deDataDefinitionField.hasCustomProperty("select")) {
			jsonObject.put(
				"select", deDataDefinitionField.getCustomProperty("select"));
		}

		return jsonObject;
	}

	protected Locale getDisplayLocale(HttpServletRequest httpServletRequest) {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return themeDisplay.getLocale();
	}

	protected FileEntry getFileEntry(JSONObject valueJSONObject) {
		try {
			return dlAppService.getFileEntryByUuidAndGroupId(
				valueJSONObject.getString("uuid"),
				valueJSONObject.getLong("groupId"));
		}
		catch (PortalException pe) {
			_log.error("Unable to retrieve file entry ", pe);

			return null;
		}
	}

	protected String getFileEntryTitle(FileEntry fileEntry) {
		if (fileEntry == null) {
			return StringPool.BLANK;
		}

		return html.escape(fileEntry.getTitle());
	}

	protected String getFileEntryURL(
		HttpServletRequest request, FileEntry fileEntry) {

		if (fileEntry == null) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(9);

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		sb.append(themeDisplay.getPathContext());

		sb.append("/documents/");
		sb.append(fileEntry.getRepositoryId());
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getFolderId());
		sb.append(StringPool.SLASH);
		sb.append(
			URLCodec.encodeURL(html.unescape(fileEntry.getTitle()), true));
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getUuid());

		return html.escape(sb.toString());
	}

	protected String getItemSelectorAuthToken(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay == null) {
			return StringPool.BLANK;
		}

		try {
			String itemSelectorAuthToken = AuthTokenUtil.getToken(
				request,
				portal.getControlPanelPlid(themeDisplay.getCompanyId()),
				PortletKeys.ITEM_SELECTOR);

			return itemSelectorAuthToken;
		}
		catch (PortalException pe) {
			_log.error("Unable to generate item selector auth token ", pe);
		}

		return StringPool.BLANK;
	}

	protected String getLexiconIconsPath(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		StringBundler sb = new StringBundler(3);

		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/lexicon/icons.svg");
		sb.append(StringPool.POUND);

		return sb.toString();
	}

	protected ResourceBundle getResourceBundle(Locale locale) {
		ResourceBundle portalResourceBundle = portal.getResourceBundle(locale);

		ResourceBundle moduleResourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return new AggregateResourceBundle(
			moduleResourceBundle, portalResourceBundle);
	}

	protected JSONObject getValueJSONObject(String value) {
		try {
			return jsonFactory.createJSONObject(value);
		}
		catch (JSONException jsone) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsone, jsone);
			}

			return null;
		}
	}

	@Reference
	protected DLAppService dlAppService;

	@Reference
	protected Html html;

	@Reference
	protected JSONFactory jsonFactory;

	@Reference
	protected Portal portal;

	private static final Log _log = LogFactoryUtil.getLog(
		DEDocumentLibraryFieldType.class);

}