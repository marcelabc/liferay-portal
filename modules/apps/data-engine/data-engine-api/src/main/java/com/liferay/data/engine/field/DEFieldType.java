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

package com.liferay.data.engine.field;

import com.liferay.data.engine.exception.DEDataDefinitionDeserializerException;
import com.liferay.data.engine.exception.DEDataDefinitionSerializerException;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.util.DEDataEngineUtil;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageConstants;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Leonardo Barros
 */
public interface DEFieldType {

	public default DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		if (!jsonObject.has("name")) {
			throw new DEDataDefinitionDeserializerException("Name is required");
		}

		if (!jsonObject.has("type")) {
			throw new DEDataDefinitionDeserializerException("Type is required");
		}

		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			jsonObject.getString("name"), jsonObject.getString("type"));

		deDataDefinitionField.setDefaultValue(jsonObject.get("defaultValue"));
		deDataDefinitionField.setIndexable(
			jsonObject.getBoolean("indexable", true));

		Map<String, String> label = DEDataEngineUtil.getLocalizedProperty(
			"label", jsonObject);

		if (label != null) {
			deDataDefinitionField.addLabels(label);
		}

		deDataDefinitionField.setLocalizable(
			jsonObject.getBoolean("localizable", false));
		deDataDefinitionField.setRepeatable(
			jsonObject.getBoolean("repeatable", false));

		Map<String, String> tips = DEDataEngineUtil.getLocalizedProperty(
			"tip", jsonObject);

		if (tips != null) {
			deDataDefinitionField.addTips(tips);
		}

		return deDataDefinitionField;
	}

	public default void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		String languageId = LanguageUtil.getLanguageId(httpServletRequest);

		context.put(
			"dir",
			LanguageUtil.get(httpServletRequest, LanguageConstants.KEY_DIR));
		context.put(
			"label",
			MapUtil.getString(deDataDefinitionField.getLabel(), languageId));
		context.put("name", deDataDefinitionField.getName());
		context.put(
			"readOnly",
			GetterUtil.getBoolean(
				deDataDefinitionField.getCustomProperty("readOnly")));
		context.put(
			"required",
			GetterUtil.getBoolean(
				deDataDefinitionField.getCustomProperty("required")));
		context.put(
			"showLabel",
			GetterUtil.getBoolean(
				deDataDefinitionField.getCustomProperty("showLabel"), true));
		context.put(
			"tip",
			MapUtil.getString(deDataDefinitionField.getTip(), languageId));
		context.put("type", deDataDefinitionField.getType());
		context.put(
			"visible",
			GetterUtil.getBoolean(
				deDataDefinitionField.getCustomProperty("visible"), true));
	}

	public default JSONObject serialize(
			DEDataDefinitionField deDataDefinitionField,
			JSONFactory jsonFactory)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = jsonFactory.createJSONObject();

		Object defaultValue = deDataDefinitionField.getDefaultValue();

		if (defaultValue != null) {
			jsonObject.put("defaultValue", defaultValue);
		}

		jsonObject.put("indexable", deDataDefinitionField.isIndexable());

		Map<String, String> label = deDataDefinitionField.getLabel();

		if (!label.isEmpty()) {
			DEDataEngineUtil.setLocalizedProperty(
				"label", jsonFactory, jsonObject, label);
		}

		jsonObject.put("localizable", deDataDefinitionField.isLocalizable());

		String name = deDataDefinitionField.getName();

		if (Validator.isNull(name)) {
			throw new DEDataDefinitionSerializerException("Name is required");
		}

		jsonObject.put("name", name);

		jsonObject.put("repeatable", deDataDefinitionField.isRepeatable());

		Map<String, String> tip = deDataDefinitionField.getTip();

		if (!tip.isEmpty()) {
			DEDataEngineUtil.setLocalizedProperty(
				"tip", jsonFactory, jsonObject, tip);
		}

		String type = deDataDefinitionField.getType();

		if ((type == null) || type.isEmpty()) {
			throw new DEDataDefinitionSerializerException("Type is required");
		}

		jsonObject.put("type", type);

		return jsonObject;
	}

}