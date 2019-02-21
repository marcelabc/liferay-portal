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
import com.liferay.data.engine.model.DEDataFieldOption;
import com.liferay.data.engine.model.DEDataFieldOptions;
import com.liferay.portal.json.JSONFactoryImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Marcela Cunha
 */
@RunWith(PowerMockRunner.class)
public class DEGridFieldTypeTest extends DEBaseFieldTypeTest {

	@Test
	public void testIncludeContext() throws Exception {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "grid");

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			"Column 1", "Value 1");
		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			"Column 2", "Value 2");

		DEDataFieldOptions columnsOptions = new DEDataFieldOptions();

		columnsOptions.setDEDataFieldOptions(
			Arrays.asList(deDataFieldOption1, deDataFieldOption2));

		deDataDefinitionField.setCustomProperty("columns", columnsOptions);

		DEDataFieldOption deDataFieldOption3 = new DEDataFieldOption(
			"Row 1", "Value 3");
		DEDataFieldOption deDataFieldOption4 = new DEDataFieldOption(
			"Row 2", "Value 4");

		DEDataFieldOptions rowsOptions = new DEDataFieldOptions();

		rowsOptions.setDEDataFieldOptions(
			Arrays.asList(deDataFieldOption3, deDataFieldOption4));

		deDataDefinitionField.setCustomProperty("rows", rowsOptions);

		Map<String, Object> fieldContext = new HashMap<>();

		DEGridFieldType gridFieldType = new DEGridFieldType();

		field(
			DEGridFieldType.class, "jsonFactory"
		).set(
			gridFieldType, new JSONFactoryImpl()
		);

		gridFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		List<DEDataFieldOption> gridColumnsOptions =
			(List<DEDataFieldOption>)fieldContext.get("columns");

		Assert.assertEquals(
			gridColumnsOptions.toString(), 2, gridColumnsOptions.size());

		DEDataFieldOption columnOption = gridColumnsOptions.get(0);

		Assert.assertEquals(
			"Column 1", columnOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 1", columnOption.getValue());

		columnOption = gridColumnsOptions.get(1);

		Assert.assertEquals(
			"Column 2", columnOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 2", columnOption.getValue());

		List<DEDataFieldOption> gridRowsOptions =
			(List<DEDataFieldOption>)fieldContext.get("rows");

		Assert.assertEquals(
			gridRowsOptions.toString(), 2, gridRowsOptions.size());

		DEDataFieldOption rowOption = gridRowsOptions.get(0);

		Assert.assertEquals(
			"Row 1", rowOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 3", rowOption.getValue());

		rowOption = gridRowsOptions.get(1);

		Assert.assertEquals(
			"Row 2", rowOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 4", rowOption.getValue());
	}

}