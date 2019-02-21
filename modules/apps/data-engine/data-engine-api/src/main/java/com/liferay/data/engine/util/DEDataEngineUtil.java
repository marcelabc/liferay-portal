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

package com.liferay.data.engine.util;

import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataFieldOption;
import com.liferay.data.engine.model.DEDataFieldOptions;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Gabriel Albuquerque
 */
public final class DEDataEngineUtil {

	public static Map<String, DEDataDefinitionField>
		getDEDataDefinitionFieldsMap(DEDataDefinition deDataDefinition) {

		List<DEDataDefinitionField> deDataDefinitionFields =
			deDataDefinition.getDEDataDefinitionFields();

		Stream<DEDataDefinitionField> stream = deDataDefinitionFields.stream();

		return stream.collect(
			Collectors.toMap(field -> field.getName(), Function.identity()));
	}

	public static Object getDEDataDefinitionFieldValue(
		DEDataDefinitionField deDataDefinitionField,
		Map<String, Object> values) {

		if (deDataDefinitionField.isLocalizable()) {
			return (Map<String, Object>)values.get(
				deDataDefinitionField.getName());
		}
		else if (deDataDefinitionField.isRepeatable()) {
			return (Object[])values.get(deDataDefinitionField.getName());
		}

		return values.get(deDataDefinitionField.getName());
	}

	public static Map<String, String> getLocalizedProperty(
		String property, JSONObject jsonObject) {

		if (!jsonObject.has(property)) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		JSONObject languageJSONObject = jsonObject.getJSONObject(property);

		Iterator<String> keys = languageJSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();

			map.put(key, languageJSONObject.getString(key));
		}

		return map;
	}

	public static String getLocalizedValue(
		DEDataDefinitionField deDataDefinitionField, String property,
		String languageId) {

		Map<String, String> map =
			(Map<String, String>)deDataDefinitionField.getCustomProperty(
				property);

		if (map == null) {
			return null;
		}

		return MapUtil.getString(map, languageId);
	}

	public static DecimalFormat getNumberFormat(Locale locale) {
		DecimalFormat formatter = _decimalFormattersMap.get(locale);

		if (formatter == null) {
			formatter = (DecimalFormat)DecimalFormat.getInstance(locale);

			formatter.setGroupingUsed(false);
			formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
			formatter.setParseBigDecimal(true);

			_decimalFormattersMap.put(locale, formatter);
		}

		return formatter;
	}

	public static List<DEDataFieldOption> getOptions(
		DEDataDefinitionField deDataDefinitionField, String property,
		String languageId) {

		DEDataFieldOptions deDataFieldOptions =
			(DEDataFieldOptions)deDataDefinitionField.getCustomProperty(
				property);

		if (deDataFieldOptions == null) {
			return null;
		}

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		for (DEDataFieldOption deDataFieldOption :
				deDataFieldOptions.getDEDataFieldOptions()) {

			deDataFieldOptionList.add(
				new DEDataFieldOption(
					deDataFieldOption.getLabel(),
					String.valueOf(deDataFieldOption.getValue()), languageId));
		}

		return deDataFieldOptionList;
	}

	public static DEDataFieldOptions getOptionsProperty(
		JSONObject jsonObject, String property) {

		if (!jsonObject.has(property)) {
			return null;
		}

		JSONObject optionsJSONObject = jsonObject.getJSONObject(property);

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		Iterator<String> keys = optionsJSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();

			Map<String, String> localizedValues = new HashMap<>();

			JSONObject languageJSONObject = optionsJSONObject.getJSONObject(
				key);

			Iterator<String> languageKeys = languageJSONObject.keys();

			while (languageKeys.hasNext()) {
				String languageId = languageKeys.next();

				localizedValues.put(
					languageId, languageJSONObject.getString(languageId));
			}

			DEDataFieldOption deDataFieldOption = new DEDataFieldOption(
				localizedValues, key);

			deDataFieldOptionList.add(deDataFieldOption);
		}

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		return deDataFieldOptions;
	}

	public static String getValue(String json, JSONFactory jsonFactory) {
		try {
			JSONArray jsonArray = jsonFactory.createJSONArray(json);

			return String.valueOf(jsonArray.get(0));
		}
		catch (JSONException jsone) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsone, jsone);
			}

			return json;
		}
	}

	public static List<String> getValues(String json, JSONFactory jsonFactory) {
		JSONArray jsonArray = null;

		try {
			jsonArray = jsonFactory.createJSONArray(json);
		}
		catch (JSONException jsone) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsone, jsone);
			}

			jsonArray = jsonFactory.createJSONArray();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(String.valueOf(jsonArray.get(i)));
		}

		return values;
	}

	public static void setLocalizedProperty(
		String property, JSONFactory jsonFactory, JSONObject jsonObject,
		Map<String, String> map) {

		JSONObject languageJSONObject = jsonFactory.createJSONObject();

		Set<Map.Entry<String, String>> set = map.entrySet();

		Stream<Map.Entry<String, String>> stream = set.stream();

		stream.forEach(
			entry -> languageJSONObject.put(entry.getKey(), entry.getValue()));

		jsonObject.put(property, languageJSONObject);
	}

	public static void setOptionsProperty(
		DEDataDefinitionField deDataDefinitionField, String property,
		JSONFactory jsonFactory, JSONObject jsonObject) {

		if (!deDataDefinitionField.hasCustomProperty(property)) {
			return;
		}

		DEDataFieldOptions deDataFieldOptions =
			(DEDataFieldOptions)deDataDefinitionField.getCustomProperty(
				property);

		List<DEDataFieldOption> deDataFieldOptionsList =
			deDataFieldOptions.getDEDataFieldOptions();

		JSONObject optionsJSONObject = jsonFactory.createJSONObject();

		jsonObject.put(property, optionsJSONObject);

		for (DEDataFieldOption deDataFieldOption : deDataFieldOptionsList) {
			Map<String, String> labels = deDataFieldOption.getLabels();

			JSONObject labelJSONObject = jsonFactory.createJSONObject();

			optionsJSONObject.put(
				String.valueOf(deDataFieldOption.getValue()), labelJSONObject);

			for (Map.Entry<String, String> entry : labels.entrySet()) {
				labelJSONObject.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DEDataEngineUtil.class);

	private static final Map<Locale, DecimalFormat> _decimalFormattersMap =
		new ConcurrentHashMap<>();

}