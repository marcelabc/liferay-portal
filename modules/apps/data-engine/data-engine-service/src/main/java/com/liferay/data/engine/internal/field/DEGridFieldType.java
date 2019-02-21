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
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=grid",
	service = DEFieldType.class
)
public class DEGridFieldType implements DEFieldType {

	@Override
	public DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinitionField deDataDefinitionField =
			DEFieldType.super.deserialize(jsonObject);

		if (jsonObject.has("columns")) {
			deDataDefinitionField.setCustomProperty(
				"columns",
				DEDataEngineUtil.getOptionsProperty(jsonObject, "columns"));
		}

		if (jsonObject.has("rows")) {
			deDataDefinitionField.setCustomProperty(
				"rows",
				DEDataEngineUtil.getOptionsProperty(jsonObject, "rows"));
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

		if (deDataDefinitionField.hasCustomProperty("columns")) {
			context.put(
				"columns",
				DEDataEngineUtil.getOptions(
					deDataDefinitionField, "columns", languageId));
		}

		if (deDataDefinitionField.hasCustomProperty("rows")) {
			context.put(
				"rows",
				DEDataEngineUtil.getOptions(
					deDataDefinitionField, "rows", languageId));
		}

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

		if (deDataDefinitionField.hasCustomProperty("columns")) {
			DEDataEngineUtil.setOptionsProperty(
				deDataDefinitionField, "columns", jsonFactory, jsonObject);
		}

		if (deDataDefinitionField.hasCustomProperty("rows")) {
			DEDataEngineUtil.setOptionsProperty(
				deDataDefinitionField, "rows", jsonFactory, jsonObject);
		}

		return jsonObject;
	}

	@Reference
	protected JSONFactory jsonFactory;

}