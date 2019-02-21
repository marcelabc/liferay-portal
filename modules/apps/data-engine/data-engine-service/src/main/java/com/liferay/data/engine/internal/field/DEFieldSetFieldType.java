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

import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=fieldset",
	service = DEFieldType.class
)
public class DEFieldSetFieldType implements DEFieldType {

	@Override
	public void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		DEFieldType.super.includeContext(
			httpServletRequest, httpServletResponse, context,
			deDataDefinitionField, readOnly);

		if (deDataDefinitionField.hasCustomProperty("nestedFields")) {
			Map<String, List<Object>> nestedFieldsMap =
				(Map<String, List<Object>>)
					deDataDefinitionField.getCustomProperty("nestedFields");

			String[] nestedFieldNames = getNestedFieldNames(
				GetterUtil.getString(
					deDataDefinitionField.getCustomProperty(
						"nestedFieldNames")),
				nestedFieldsMap.keySet());

			List<Object> nestedFields = getNestedFields(
				nestedFieldsMap, nestedFieldNames);

			context.put("nestedFields", nestedFields);

			String orientation = GetterUtil.getString(
				deDataDefinitionField.getCustomProperty("orientation"),
				"horizontal");

			int columnSize = getColumnSize(nestedFields.size(), orientation);

			context.put("columnSize", columnSize);
		}

		if (context.containsKey("label")) {
			context.put("showLabel", true);
		}
	}

	protected int getColumnSize(int nestedFieldsSize, String orientation) {
		if (Objects.equals(orientation, "vertical")) {
			return 12;
		}

		if (nestedFieldsSize == 0) {
			return 0;
		}

		return 12 / nestedFieldsSize;
	}

	protected String[] getNestedFieldNames(
		String nestedFieldNames, Set<String> defaultNestedFieldNames) {

		if (Validator.isNotNull(nestedFieldNames)) {
			return StringUtil.split(nestedFieldNames);
		}

		return defaultNestedFieldNames.toArray(
			new String[defaultNestedFieldNames.size()]);
	}

	protected List<Object> getNestedFields(
		Map<String, List<Object>> nestedFieldsMap, String[] nestedFieldNames) {

		List<Object> nestedFields = new ArrayList<>();

		for (String nestedFieldName : nestedFieldNames) {
			nestedFields.addAll(nestedFieldsMap.get(nestedFieldName));
		}

		return nestedFields;
	}

}