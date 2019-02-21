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
import com.liferay.data.engine.util.DEDataEngineUtil;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=password",
	service = DEFieldType.class
)
public class DEPasswordFieldType implements DEFieldType {

	@Override
	public DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinitionField deDataDefinitionField =
			DEFieldType.super.deserialize(jsonObject);

		if (jsonObject.has("placeholder")) {
			deDataDefinitionField.setCustomProperty(
				"placeholder",
				DEDataEngineUtil.getLocalizedProperty(
					"placeholder", jsonObject));
		}

		if (jsonObject.has("tooltip")) {
			deDataDefinitionField.setCustomProperty(
				"tooltip",
				DEDataEngineUtil.getLocalizedProperty("tooltip", jsonObject));
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

		String languageId = LanguageUtil.getLanguageId(httpServletRequest);

		if (deDataDefinitionField.hasCustomProperty("placeholder")) {
			context.put(
				"placeholder",
				DEDataEngineUtil.getLocalizedValue(
					deDataDefinitionField, "placeholder", languageId));
		}

		if (deDataDefinitionField.hasCustomProperty("tooltip")) {
			context.put(
				"tooltip",
				DEDataEngineUtil.getLocalizedValue(
					deDataDefinitionField, "tooltip", languageId));
		}
	}

	@Override
	public JSONObject serialize(
			DEDataDefinitionField deDataDefinitionField,
			JSONFactory jsonFactory)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = DEFieldType.super.serialize(
			deDataDefinitionField, jsonFactory);

		if (deDataDefinitionField.hasCustomProperty("placeholder")) {
			Map<String, String> placeholder =
				(Map<String, String>)deDataDefinitionField.getCustomProperty(
					"placeholder");

			DEDataEngineUtil.setLocalizedProperty(
				"placeholder", jsonFactory, jsonObject, placeholder);
		}

		if (deDataDefinitionField.hasCustomProperty("tooltip")) {
			Map<String, String> tooltip =
				(Map<String, String>)deDataDefinitionField.getCustomProperty(
					"tooltip");

			DEDataEngineUtil.setLocalizedProperty(
				"tooltip", jsonFactory, jsonObject, tooltip);
		}

		return jsonObject;
	}

}