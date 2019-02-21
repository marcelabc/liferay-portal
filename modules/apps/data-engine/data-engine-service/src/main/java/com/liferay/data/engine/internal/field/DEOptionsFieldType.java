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
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=options",
	service = DEFieldType.class
)
public class DEOptionsFieldType implements DEFieldType {

	@Override
	public DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinitionField deDataDefinitionField =
			DEFieldType.super.deserialize(jsonObject);

		if (jsonObject.has("allowEmptyOptions")) {
			deDataDefinitionField.setCustomProperty(
				"allowEmptyOptions",
				jsonObject.getBoolean("allowEmptyOptions"));
		}

		if (jsonObject.has("value")) {
			deDataDefinitionField.setCustomProperty(
				"value",
				DEDataEngineUtil.getOptionsProperty(jsonObject, "value"));
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

		if (deDataDefinitionField.hasCustomProperty("allowEmptyOptions")) {
			context.put(
				"allowEmptyOptions",
				GetterUtil.getBoolean(
					deDataDefinitionField.getCustomProperty(
						"allowEmptyOptions")));
		}

		context.put("defaultLanguageId", languageId);

		if (deDataDefinitionField.hasCustomProperty("value")) {
			context.put(
				"value",
				DEDataEngineUtil.getOptions(
					deDataDefinitionField, "value", languageId));
		}
	}

	@Override
	public JSONObject serialize(
			DEDataDefinitionField deDataDefinitionField,
			JSONFactory jsonFactory)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = DEFieldType.super.serialize(
			deDataDefinitionField, jsonFactory);

		if (deDataDefinitionField.hasCustomProperty("allowEmptyOptions")) {
			jsonObject.put(
				"allowEmptyOptions",
				GetterUtil.getBoolean(
					deDataDefinitionField.getCustomProperty(
						"allowEmptyOptions")));
		}

		if (deDataDefinitionField.hasCustomProperty("value")) {
			DEDataEngineUtil.setOptionsProperty(
				deDataDefinitionField, "value", jsonFactory, jsonObject);
		}

		return jsonObject;
	}

}