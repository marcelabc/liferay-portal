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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.function;

import com.liferay.dynamic.data.mapping.constants.DDMConstants;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	factory = DDMConstants.EXPRESSION_FUNCTION_FACTORY_NAME,
	service = DDMExpressionFunction.Function2.class
)
public class ContainsFunction
	implements DDMExpressionFunction.Function2<Object, String, Boolean> {

	@Override
	public Boolean apply(Object object, String key) {
		if (object == null) {
			return false;
		}

		if (object instanceof JSONArray) {
			List<String> objectList = _toList(object.toString());
			List<String> keyList = _toList(key.toString());
			
			return objectList.containsAll(keyList);
			
			//return apply(object.toString(), key);
		}

		if (object instanceof String) {
			return apply((String)object, key);
		}

		return false;
	}

	@Override
	public String getName() {
		return "contains";
	}

	protected Boolean apply(String string1, String string2) {
		if (Validator.isNull(string1) || Validator.isNull(string2)) {
			return false;
		}

		string1 = StringUtil.toLowerCase(string1);
		string2 = StringUtil.toLowerCase(string2);

		return string1.contains(string2);
	}
	
	private List<String> _toList(String string) {
		String value = string.substring(1, string.length() - 1);		
		String[] values = StringUtil.split(value);
		return ListUtil.toList(values);
	}

}