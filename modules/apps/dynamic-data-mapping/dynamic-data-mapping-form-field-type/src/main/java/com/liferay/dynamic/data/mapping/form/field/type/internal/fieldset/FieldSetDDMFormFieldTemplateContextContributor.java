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

package com.liferay.dynamic.data.mapping.form.field.type.internal.fieldset;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.service.DDMStructureLayoutLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Lancha
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=fieldset",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		FieldSetDDMFormFieldTemplateContextContributor.class
	}
)
public class FieldSetDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, List<Object>> nestedFieldsMap =
			(Map<String, List<Object>>)ddmFormFieldRenderingContext.getProperty(
				"nestedFields");

		List<Object> nestedFields = new ArrayList<>();

		if (nestedFieldsMap == null) {
			nestedFields.addAll(
				getNestedFields(
					(List<Object>)ddmFormFieldRenderingContext.getProperty(
						"fields")));
		}
		else {
			nestedFields = getNestedFields(
				nestedFieldsMap,
				getNestedFieldNames(
					GetterUtil.getString(
						ddmFormField.getProperty("nestedFieldNames")),
					nestedFieldsMap.keySet()));
		}

		JSONArray rows = getJSONArray(
			GetterUtil.getString(ddmFormField.getProperty("rows")));

		if (rows.length() == 0) {
			rows = getRows(
				getLayoutDefinition(
					GetterUtil.getLong(
						ddmFormField.getProperty("ddmStructureId")),
					GetterUtil.getLong(
						ddmFormField.getProperty("ddmStructureLayoutId"))));
		}

		return HashMapBuilder.<String, Object>put(
			"columnSize", DDMFormLayoutColumn.FULL
		).put(
			"ddmStructureId", ddmFormField.getProperty("ddmStructureId")
		).put(
			"ddmStructureLayoutId",
			ddmFormField.getProperty("ddmStructureLayoutId")
		).put(
			"nestedFields", nestedFields
		).put(
			"rows", rows
		).build();
	}

	protected int countVisibleNestedFields(List<Object> nestedFields) {
		Stream<Object> stream = nestedFields.stream();

		return GetterUtil.getInteger(
			stream.filter(
				this::_isNestedFieldVisible
			).count());
	}

	protected JSONObject createRowJSONObject(List<Object> nestedFields) {
		int columnSize = 12 / nestedFields.size();

		JSONArray columnsJSONArray = jsonFactory.createJSONArray();

		for (Object nestedFieldContext : nestedFields) {
			JSONArray fieldsJSONArray = jsonFactory.createJSONArray();

			fieldsJSONArray.put(
				MapUtil.getString(
					(Map<String, ?>)nestedFieldContext, "fieldName"));

			JSONObject columnJSONObject = jsonFactory.createJSONObject();

			columnJSONObject.put(
				"fields", fieldsJSONArray
			).put(
				"size", columnSize
			);

			columnsJSONArray.put(columnJSONObject);
		}

		JSONObject rowJSONObject = jsonFactory.createJSONObject();

		rowJSONObject.put("columns", columnsJSONArray);

		return rowJSONObject;
	}

	protected DDMForm getDDMForm(DDMStructure ddmStructure) {
		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				jsonDDMFormDeserializer.deserialize(
					DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
						ddmStructure.getDefinition()
					).build());

		return ddmFormDeserializerDeserializeResponse.getDDMForm();
	}

	protected DDMFormLayout getDDMFormLayout(
		DDMStructureLayout ddmStructureLayout) {

		DDMFormLayoutDeserializerDeserializeResponse
			ddmFormLayoutDeserializerDeserializeResponse =
				ddmFormLayoutDeserializer.deserialize(
					DDMFormLayoutDeserializerDeserializeRequest.Builder.
						newBuilder(
							ddmStructureLayout.getDefinition()
						).build());

		return ddmFormLayoutDeserializerDeserializeResponse.getDDMFormLayout();
	}

	protected JSONArray getJSONArray(String rows) {
		try {
			return jsonFactory.createJSONArray(rows);
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException, jsonException);
			}
		}

		return jsonFactory.createJSONArray();
	}

	protected String getLayoutDefinition(
		Long ddmStructureId, Long ddmStructureLayoutId) {

		try {
			DDMStructure ddmStructure = ddmStructureLocalService.getStructure(
				ddmStructureId);

			DDMForm ddmForm = ddmStructureLocalService.getStructureDDMForm(
				ddmStructure);

			List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

			ddmFormFields = ListUtil.filter(
				ddmFormFields,
				ddmFormField -> Validator.isNotNull(
					ddmFormField.getProperty("ddmStructureId")));

			String layoutDefinition = StringPool.BLANK;

			if (!ddmFormFields.isEmpty()) {
				DDMFormField ddmFormField = ddmFormFields.get(0);

				layoutDefinition = getLayoutDefinition(
					GetterUtil.getLong(
						ddmFormField.getProperty("ddmStructureId")),
					GetterUtil.getLong(
						ddmFormField.getProperty("ddmStructureLayoutId")));
			}

			DDMStructureLayout ddmStructureLayout =
				ddmStructureLayoutLocalService.getStructureLayout(
					ddmStructureLayoutId);

			return mergeLayoutsDefinitions(
				ddmStructureLayout.getDefinition(), layoutDefinition);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException, portalException);
			}
		}

		return StringPool.BLANK;
	}

	protected String[] getNestedFieldNames(
		String nestedFieldNames, Set<String> defaultNestedFieldNames) {

		if (Validator.isNotNull(nestedFieldNames)) {
			return StringUtil.split(nestedFieldNames);
		}

		return defaultNestedFieldNames.toArray(new String[0]);
	}

	protected List<Object> getNestedFields(List<Object> fields) {
		List<Object> nestedFields = new ArrayList<>();

		for (int i = 0; i < fields.size(); i++) {
			HashMap<String, Object> field = (HashMap<String, Object>)fields.get(
				i);

			if (!Objects.equals(field.get("type"), "fieldset")) {
				nestedFields.add(field);
			}
			else if (Validator.isNotNull(field.get("nestedFields"))) {
				nestedFields.addAll((List<Object>)field.get("nestedFields"));
			}
		}

		return nestedFields;
	}

	protected List<Object> getNestedFields(
		Map<String, List<Object>> nestedFieldsMap, String[] nestedFieldNames) {

		List<Object> nestedFields = new ArrayList<>();

		for (String nestedFieldName : nestedFieldNames) {
			nestedFields.addAll(nestedFieldsMap.get(nestedFieldName));
		}

		return nestedFields;
	}

	protected JSONArray getRows(String layoutDefinition) {
		try {
			JSONObject definition = JSONFactoryUtil.createJSONObject(
				layoutDefinition);

			JSONArray pagesJSONArray = definition.getJSONArray("pages");

			JSONObject pageJSONObject = pagesJSONArray.getJSONObject(0);

			JSONArray rowsJSONArray = pageJSONObject.getJSONArray("rows");

			JSONArray rows = JSONFactoryUtil.createJSONArray();

			if (rowsJSONArray != null) {
				for (int i = 0; i < rowsJSONArray.length(); i++) {
					JSONObject rowJSONObject = rowsJSONArray.getJSONObject(i);

					JSONArray columnsJSONArray = rowJSONObject.getJSONArray(
						"columns");

					JSONArray columns = JSONFactoryUtil.createJSONArray();

					if (columnsJSONArray != null) {
						for (int j = 0; j < columnsJSONArray.length(); j++) {
							JSONObject columnJSONObject =
								columnsJSONArray.getJSONObject(j);

							columns.put(
								JSONUtil.put(
									"fields", columnJSONObject.get("fieldNames")
								).put(
									"size", columnJSONObject.get("size")
								));
						}
					}

					rows.put(JSONUtil.put("columns", columns));
				}
			}

			return rows;
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException, jsonException);
			}
		}

		return jsonFactory.createJSONArray();
	}

	protected JSONArray getRowsJSONArray(
		DDMFormField ddmFormField, List<Object> nestedFields) {

		String rows = GetterUtil.getString(ddmFormField.getProperty("rows"));

		if (Validator.isNotNull(rows) || nestedFields.isEmpty()) {
			return getJSONArray(rows);
		}

		JSONArray rowsJSONArray = jsonFactory.createJSONArray();

		Stream<Object> visibleFieldsStream = nestedFields.stream();

		List<Object> visibleNestedFields = visibleFieldsStream.filter(
			this::_isNestedFieldVisible
		).collect(
			Collectors.toList()
		);

		if (!visibleNestedFields.isEmpty()) {
			rowsJSONArray.put(createRowJSONObject(visibleNestedFields));
		}

		Stream<Object> invisibleFieldsStream = nestedFields.stream();

		List<Object> invisibleNestedFields = invisibleFieldsStream.filter(
			nestedFieldContext -> !_isNestedFieldVisible(nestedFieldContext)
		).collect(
			Collectors.toList()
		);

		if (!invisibleNestedFields.isEmpty()) {
			rowsJSONArray.put(createRowJSONObject(invisibleNestedFields));
		}

		return rowsJSONArray;
	}

	protected String mergeLayoutsDefinitions(
		String definition, String parentDefinition) {

		if (!parentDefinition.isEmpty()) {
			try {
				JSONObject definitionJSONObject =
					JSONFactoryUtil.createJSONObject(definition);

				JSONObject parentDefinitionJSONObject =
					JSONFactoryUtil.createJSONObject(parentDefinition);

				JSONArray pagesJSONArray = definitionJSONObject.getJSONArray(
					"pages");

				JSONArray parentPagesJSONArray =
					parentDefinitionJSONObject.getJSONArray("pages");

				JSONObject parentPageJSONObject =
					parentPagesJSONArray.getJSONObject(0);

				JSONArray parentRowsJSONArray =
					parentPageJSONObject.getJSONArray("rows");

				if (pagesJSONArray != null) {
					JSONObject pageJSONObject = pagesJSONArray.getJSONObject(0);

					JSONArray rowsJSONArray = pageJSONObject.getJSONArray(
						"rows");

					if (rowsJSONArray != null) {
						for (int i = 1; i < rowsJSONArray.length(); i++) {
							parentRowsJSONArray.put(
								rowsJSONArray.getJSONObject(i));
						}
					}

					pageJSONObject.put("rows", parentRowsJSONArray);

					JSONArray upgradedPagesJSONArray =
						jsonFactory.createJSONArray();

					upgradedPagesJSONArray.put(pageJSONObject);

					definitionJSONObject.put("pages", upgradedPagesJSONArray);
				}

				return definitionJSONObject.toString();
			}
			catch (JSONException jsonException) {
				if (_log.isDebugEnabled()) {
					_log.debug(jsonException, jsonException);
				}
			}
		}

		return definition;
	}

	@Reference
	protected DDMFormLayoutDeserializer ddmFormLayoutDeserializer;

	@Reference
	protected DDMStructureLayoutLocalService ddmStructureLayoutLocalService;

	@Reference
	protected DDMStructureLocalService ddmStructureLocalService;

	@Reference(target = "(ddm.form.deserializer.type=json)")
	protected DDMFormDeserializer jsonDDMFormDeserializer;

	@Reference
	protected JSONFactory jsonFactory;

	private boolean _isNestedFieldVisible(Object nestedFieldContext) {
		return MapUtil.getBoolean(
			(Map<String, ?>)nestedFieldContext, "visible", true);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FieldSetDDMFormFieldTemplateContextContributor.class);

}