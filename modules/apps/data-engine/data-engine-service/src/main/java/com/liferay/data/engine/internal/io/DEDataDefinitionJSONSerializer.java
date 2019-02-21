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

import com.liferay.data.engine.exception.DEDataDefinitionSerializerException;
import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.internal.field.DEFieldTypeTracker;
import com.liferay.data.engine.io.DEDataDefinitionSerializer;
import com.liferay.data.engine.io.DEDataDefinitionSerializerApplyRequest;
import com.liferay.data.engine.io.DEDataDefinitionSerializerApplyResponse;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataDefinitionRule;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "data.definition.serializer.type=json",
	service = DEDataDefinitionSerializer.class
)
public class DEDataDefinitionJSONSerializer
	implements DEDataDefinitionSerializer {

	@Override
	public DEDataDefinitionSerializerApplyResponse apply(
			DEDataDefinitionSerializerApplyRequest
				deDataDefinitionSerializerApplyRequest)
		throws DEDataDefinitionSerializerException {

		DEDataDefinition deDataDefinition =
			deDataDefinitionSerializerApplyRequest.getDEDataDefinition();

		JSONObject jsonObject = jsonFactory.createJSONObject();

		jsonObject.put(
			"fields",
			getDEDataDefinitionFieldsJSONArray(
				deDataDefinition.getDEDataDefinitionFields()));

		List<DEDataDefinitionRule> deDataDefinitionRules =
			deDataDefinition.getDEDataDefinitionRules();

		if (!deDataDefinitionRules.isEmpty()) {
			jsonObject.put(
				"rules",
				getDEDataDefinitionRulesJSONArray(deDataDefinitionRules));
		}

		return DEDataDefinitionSerializerApplyResponse.Builder.of(
			jsonObject.toJSONString());
	}

	protected JSONObject getDEDataDefinitionFieldJSONObject(
			DEDataDefinitionField deDataDefinitionField)
		throws DEDataDefinitionSerializerException {

		DEFieldType deFieldType = deFieldTypeTracker.getDEFieldType(
			deDataDefinitionField.getType());

		return deFieldType.serialize(deDataDefinitionField, jsonFactory);
	}

	protected JSONArray getDEDataDefinitionFieldNamesJSONArray(
		List<String> deDataDefinitionFieldNames) {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		for (String deDataDefinitionFieldName : deDataDefinitionFieldNames) {
			jsonArray.put(deDataDefinitionFieldName);
		}

		return jsonArray;
	}

	protected JSONArray getDEDataDefinitionFieldsJSONArray(
			List<DEDataDefinitionField> deDataDefinitionFields)
		throws DEDataDefinitionSerializerException {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		for (DEDataDefinitionField deDataDefinitionField :
				deDataDefinitionFields) {

			jsonArray.put(
				getDEDataDefinitionFieldJSONObject(deDataDefinitionField));
		}

		return jsonArray;
	}

	protected JSONObject getDEDataDefinitionRuleJSONObject(
		DEDataDefinitionRule deDataDefinitionRule) {

		JSONObject jsonObject = jsonFactory.createJSONObject();

		jsonObject.put(
			"fields",
			getDEDataDefinitionFieldNamesJSONArray(
				deDataDefinitionRule.getDEDataDefinitionFieldNames()));
		jsonObject.put("name", deDataDefinitionRule.getName());

		jsonObject.put("ruleType", deDataDefinitionRule.getRuleType());

		Map<String, Object> parameters = deDataDefinitionRule.getParameters();

		if (!parameters.isEmpty()) {
			JSONObject parametersJSONObject = jsonFactory.createJSONObject();

			jsonObject.put("parameters", parametersJSONObject);

			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				parametersJSONObject.put(entry.getKey(), entry.getValue());
			}
		}

		return jsonObject;
	}

	protected JSONArray getDEDataDefinitionRulesJSONArray(
		List<DEDataDefinitionRule> deDataDefinitionRules) {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		Stream<DEDataDefinitionRule> stream = deDataDefinitionRules.stream();

		stream.map(
			this::getDEDataDefinitionRuleJSONObject
		).forEach(
			jsonArray::put
		);

		return jsonArray;
	}

	@Reference
	protected DEFieldTypeTracker deFieldTypeTracker;

	@Reference
	protected JSONFactory jsonFactory;

}