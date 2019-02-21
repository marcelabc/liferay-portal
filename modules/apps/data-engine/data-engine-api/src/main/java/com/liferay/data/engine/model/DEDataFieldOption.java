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

package com.liferay.data.engine.model;

import com.liferay.portal.kernel.util.MapUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DEDataFieldOption {

	public DEDataFieldOption(Map<String, String> labels, Object value) {
		_labels.putAll(labels);
		_value = value;
	}

	public DEDataFieldOption(String label, Object value) {
		_labels.put(_DEFAULT, label);
		_value = value;
	}

	public DEDataFieldOption(String label, Object value, String languageId) {
		_labels.put(languageId, label);
		_value = value;
	}

	public String getLabel() {
		return MapUtil.getString(_labels, _DEFAULT);
	}

	public String getLabel(String languageId) {
		return MapUtil.getString(_labels, languageId);
	}

	public Map<String, String> getLabels() {
		return Collections.unmodifiableMap(_labels);
	}

	public Object getValue() {
		return _value;
	}

	public boolean isLocalized() {
		return !_labels.containsKey(_DEFAULT);
	}

	private static final String _DEFAULT = "DEFAULT";

	private Map<String, String> _labels = new HashMap<>();
	private final Object _value;

}