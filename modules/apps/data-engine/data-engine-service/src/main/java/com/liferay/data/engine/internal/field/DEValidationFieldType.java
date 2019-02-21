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
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=validation",
	service = DEFieldType.class
)
public class DEValidationFieldType implements DEFieldType {

	@Override
	public void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		DEFieldType.super.includeContext(
			httpServletRequest, httpServletResponse, context,
			deDataDefinitionField, readOnly);

		context.put("value", getValue(deDataDefinitionField));
	}

	protected Map<String, String> getValue(
		DEDataDefinitionField deDataDefinitionField) {

		Map<String, String> value = new HashMap<>();

		String valueString = GetterUtil.getString(
			deDataDefinitionField.getCustomProperty("value"));

		if (Validator.isNotNull(valueString)) {
			try {
				JSONObject valueJSONObject = jsonFactory.createJSONObject(
					valueString);

				value.put(
					"errorMessage", valueJSONObject.getString("errorMessage"));
				value.put(
					"expression", valueJSONObject.getString("expression"));
			}
			catch (JSONException jsone) {
				if (_log.isWarnEnabled()) {
					_log.warn(jsone, jsone);
				}
			}
		}
		else {
			value.put("errorMessage", StringPool.BLANK);
			value.put("expression", StringPool.BLANK);
		}

		return value;
	}

	@Reference
	protected JSONFactory jsonFactory;

	private static final Log _log = LogFactoryUtil.getLog(
		DEValidationFieldType.class);

}