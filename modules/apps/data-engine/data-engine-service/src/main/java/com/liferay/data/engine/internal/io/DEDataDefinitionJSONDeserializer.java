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

package com.liferay.data.engine.internal.io;

import com.liferay.data.engine.exception.DEDataDefinitionDeserializerException;
import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.internal.field.DEFieldTypeTracker;
import com.liferay.data.engine.io.DEDataDefinitionDeserializer;
import com.liferay.data.engine.io.DEDataDefinitionDeserializerApplyRequest;
import com.liferay.data.engine.io.DEDataDefinitionDeserializerApplyResponse;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataDefinitionRule;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "data.definition.deserializer.type=json",
	service = DEDataDefinitionDeserializer.class
)
public class DEDataDefinitionJSONDeserializer
	implements DEDataDefinitionDeserializer {

	@Override
	public DEDataDefinitionDeserializerApplyResponse apply(
			DEDataDefinitionDeserializerApplyRequest
				deDataDefinitionDeserializerApplyRequest)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinition deDataDefinition = new DEDataDefinition();

		try {
			JSONObject jsonObject = jsonFactory.createJSONObject(
				deDataDefinitionDeserializerApplyRequest.getContent());

			deDataDefinition.setDEDataDefinitionFields(
				getDEDataDefinitionFields(jsonObject.getJSONArray("fields")));

			if (jsonObject.has("rules")) {
				deDataDefinition.setDEDataDefinitionRules(
					getDEDataDefinitionRules(jsonObject.getJSONArray("rules")));
			}

			return DEDataDefinitionDeserializerApplyResponse.Builder.of(
				deDataDefinition);
		}
		catch (JSONException jsone) {
			throw new DEDataDefinitionDeserializerException(
				"Invalid JSON format");
		}
		catch (DEDataDefinitionDeserializerException deddde) {
			throw deddde;
		}
	}

	protected DEDataDefinitionField getDEDataDefinitionField(
			JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		DEFieldType deFieldType = deFieldTypeTracker.getDEFieldType(
			jsonObject.getString("type"));

		return deFieldType.deserialize(jsonObject);
	}

	protected List<String> getDEDataDefinitionFieldNames(JSONArray jsonArray) {
		List<String> fieldNames = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			fieldNames.add(jsonArray.getString(i));
		}

		return fieldNames;
	}

	protected List<DEDataDefinitionField> getDEDataDefinitionFields(
			JSONArray jsonArray)
		throws DEDataDefinitionDeserializerException {

		List<DEDataDefinitionField> deDataDefinitionFields = new ArrayList<>(
			jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			DEDataDefinitionField deDataDefinitionField =
				getDEDataDefinitionField(jsonArray.getJSONObject(i));

			deDataDefinitionFields.add(deDataDefinitionField);
		}

		return deDataDefinitionFields;
	}

	protected DEDataDefinitionRule getDEDataDefinitionRule(
		JSONObject jsonObject) {

		DEDataDefinitionRule deDataDefinitionRule = new DEDataDefinitionRule(
			jsonObject.getString("name"), jsonObject.getString("ruleType"),
			getDEDataDefinitionFieldNames(jsonObject.getJSONArray("fields")));

		if (jsonObject.has("parameters")) {
			JSONObject parametersJSONObject = jsonObject.getJSONObject(
				"parameters");

			Iterator<String> keys = parametersJSONObject.keys();

			while (keys.hasNext()) {
				String key = keys.next();

				deDataDefinitionRule.addParameter(
					key, parametersJSONObject.get(key));
			}
		}

		return deDataDefinitionRule;
	}

	protected List<DEDataDefinitionRule> getDEDataDefinitionRules(
		JSONArray jsonArray) {

		List<DEDataDefinitionRule> deDataDefinitionRules = new ArrayList<>(
			jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			DEDataDefinitionRule deDataDefinitionRule = getDEDataDefinitionRule(
				jsonArray.getJSONObject(i));

			deDataDefinitionRules.add(deDataDefinitionRule);
		}

		return deDataDefinitionRules;
	}

	@Reference
	protected DEFieldTypeTracker deFieldTypeTracker;

	@Reference
	protected JSONFactory jsonFactory;

}