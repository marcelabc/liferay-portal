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
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=numeric",
	service = DEFieldType.class
)
public class DENumericFieldType implements DEFieldType {

	@Override
	public DEDataDefinitionField deserialize(JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinitionField deDataDefinitionField =
			DEFieldType.super.deserialize(jsonObject);

		if (jsonObject.has("dataType")) {
			deDataDefinitionField.setCustomProperty(
				"dataType", jsonObject.getString("dataType"));
		}

		if (jsonObject.has("placeholder")) {
			deDataDefinitionField.setCustomProperty(
				"placeholder",
				DEDataEngineUtil.getLocalizedProperty(
					"placeholder", jsonObject));
		}

		if (jsonObject.has("predefinedValue")) {
			deDataDefinitionField.setCustomProperty(
				"predefinedValue", jsonObject.getDouble("predefinedValue"));
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

		context.put(
			"dataType",
			GetterUtil.getString(
				deDataDefinitionField.getCustomProperty("dataType"),
				"decimal"));

		if (deDataDefinitionField.hasCustomProperty("placeholder")) {
			context.put(
				"placeholder",
				DEDataEngineUtil.getLocalizedValue(
					deDataDefinitionField, "placeholder", languageId));
		}

		if (deDataDefinitionField.hasCustomProperty("predefinedValue")) {
			Map<String, Object> predefinedValue =
				(Map<String, Object>)deDataDefinitionField.getCustomProperty(
					"predefinedValue");

			context.put(
				"predefinedValue",
				getFormattedValue(
					predefinedValue.get(languageId),
					httpServletRequest.getLocale()));
		}

		context.put("symbols", getSymbolsMap(httpServletRequest.getLocale()));

		if (deDataDefinitionField.hasCustomProperty("tooltip")) {
			context.put(
				"tooltip",
				DEDataEngineUtil.getLocalizedValue(
					deDataDefinitionField, "tooltip", languageId));
		}

		Object value = deDataDefinitionField.getCustomProperty("value");

		if ((value == null) || Objects.equals(value, "NaN")) {
			context.put("value", "");
		}
		else {
			context.put(
				"value",
				getFormattedValue(value, httpServletRequest.getLocale()));
		}
	}

	@Override
	public JSONObject serialize(
			DEDataDefinitionField deDataDefinitionField,
			JSONFactory jsonFactory)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = DEFieldType.super.serialize(
			deDataDefinitionField, jsonFactory);

		if (deDataDefinitionField.hasCustomProperty("dataType")) {
			jsonObject.put(
				"dataType",
				deDataDefinitionField.getCustomProperty("dataType"));
		}

		if (deDataDefinitionField.hasCustomProperty("placeholder")) {
			Map<String, String> placeholder =
				(Map<String, String>)deDataDefinitionField.getCustomProperty(
					"placeholder");

			DEDataEngineUtil.setLocalizedProperty(
				"placeholder", jsonFactory, jsonObject, placeholder);
		}

		if (deDataDefinitionField.hasCustomProperty("predefinedValue")) {
			jsonObject.put(
				"predefinedValue",
				deDataDefinitionField.getCustomProperty("predefinedValue"));
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

	protected String getFormattedValue(Object value, Locale locale) {
		if (value == null) {
			return StringPool.BLANK;
		}

		DecimalFormat numberFormat = DEDataEngineUtil.getNumberFormat(locale);

		return numberFormat.format(GetterUtil.getNumber(value));
	}

	protected Map<String, String> getSymbolsMap(Locale locale) {
		DecimalFormat formatter = DEDataEngineUtil.getNumberFormat(locale);

		DecimalFormatSymbols decimalFormatSymbols =
			formatter.getDecimalFormatSymbols();

		Map<String, String> symbolsMap = new HashMap<>();

		symbolsMap.put(
			"decimalSymbol",
			String.valueOf(decimalFormatSymbols.getDecimalSeparator()));
		symbolsMap.put(
			"thousandsSeparator",
			String.valueOf(decimalFormatSymbols.getGroupingSeparator()));

		return symbolsMap;
	}

}