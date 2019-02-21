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

import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Marcela Cunha
 */
@RunWith(PowerMockRunner.class)
public class DEFieldSetFieldTypeTest extends DEBaseFieldTypeTest {

	@Test
	public void testIncludeContextWithHorizontalOrientation() {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "fieldSet");

		Map<String, Object> nestedField0 = new HashMap<>();

		nestedField0.put("name", "field0");
		nestedField0.put("type", "text");

		Map<String, Object> nestedField1 = new HashMap<>();

		nestedField1.put("name", "field1");
		nestedField1.put("type", "checkbox");

		Map<String, List<Object>> nestedFields = new HashMap<>();

		nestedFields.put("field0", Arrays.<Object>asList(nestedField0));
		nestedFields.put("field1", Arrays.<Object>asList(nestedField1));

		deDataDefinitionField.setCustomProperty("nestedFields", nestedFields);

		deDataDefinitionField.setCustomProperty("orientation", "horizontal");

		Map<String, Object> fieldContext = new HashMap<>();

		DEFieldSetFieldType fieldSetFieldType = new DEFieldSetFieldType();

		fieldSetFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals(6, MapUtil.getInteger(fieldContext, "columnSize"));

		Assert.assertTrue(fieldContext.containsKey("nestedFields"));

		List<Map<String, Object>> nestedFieldsProperty =
			(List<Map<String, Object>>)fieldContext.get("nestedFields");

		Assert.assertEquals(
			nestedFieldsProperty.toString(), 2, nestedFieldsProperty.size());

		Assert.assertTrue(nestedFieldsProperty.contains(nestedField0));
		Assert.assertTrue(nestedFieldsProperty.contains(nestedField1));
	}

	@Test
	public void testIncludeContextWithVerticalOrientation() throws Exception {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "fieldSet");

		Map<String, Object> nestedField0 = new HashMap<>();

		nestedField0.put("name", "field0");
		nestedField0.put("type", "text");

		Map<String, Object> nestedField1 = new HashMap<>();

		nestedField1.put("name", "field1");
		nestedField1.put("type", "checkbox");

		Map<String, Object> nestedField2 = new HashMap<>();

		nestedField2.put("name", "field2");
		nestedField2.put("type", "select");

		Map<String, List<Object>> nestedFields = new HashMap<>();

		nestedFields.put("field0", Arrays.<Object>asList(nestedField0));
		nestedFields.put("field1", Arrays.<Object>asList(nestedField1));
		nestedFields.put("field2", Arrays.<Object>asList(nestedField2));

		deDataDefinitionField.setCustomProperty("nestedFields", nestedFields);

		deDataDefinitionField.setCustomProperty("orientation", "vertical");

		Map<String, Object> fieldContext = new HashMap<>();

		DEFieldSetFieldType fieldSetFieldType = new DEFieldSetFieldType();

		HttpServletRequest request = mock(HttpServletRequest.class);

		HttpServletResponse response = mock(HttpServletResponse.class);

		when(
			language.getLanguageId(Matchers.any(HttpServletRequest.class))
		).thenReturn(
			"pt_BR"
		);

		fieldSetFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals(12, MapUtil.getInteger(fieldContext, "columnSize"));

		Assert.assertTrue(fieldContext.containsKey("nestedFields"));

		List<Map<String, Object>> nestedFieldsProperty =
			(List<Map<String, Object>>)fieldContext.get("nestedFields");

		Assert.assertEquals(
			nestedFieldsProperty.toString(), 3, nestedFieldsProperty.size());

		Assert.assertTrue(nestedFieldsProperty.contains(nestedField0));
		Assert.assertTrue(nestedFieldsProperty.contains(nestedField1));
		Assert.assertTrue(nestedFieldsProperty.contains(nestedField2));
	}

}