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
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true, service = DEFieldTypeTracker.class)
public class DEFieldTypeTracker {

	public DEFieldType getDEFieldType(String type) {
		return _deFieldTypes.get(type);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addDEFieldType(
		DEFieldType deFieldType, Map<String, Object> properties) {

		String type = MapUtil.getString(
			properties, "de.data.definition.field.type");

		_deFieldTypes.put(type, deFieldType);
	}

	@Deactivate
	protected void deactivate() {
		_deFieldTypes.clear();
	}

	protected void removeDEFieldType(
		DEFieldType deFieldType, Map<String, Object> properties) {

		String type = MapUtil.getString(
			properties, "de.data.definition.field.type");

		_deFieldTypes.remove(type);
	}

	private final Map<String, DEFieldType> _deFieldTypes = new TreeMap<>();

}