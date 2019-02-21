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
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class DEOptionsFieldTypeTest extends DEBaseFieldTypeTest {

	@Test
	public void testIncludeContext() throws Exception {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "options");

		deDataDefinitionField.setCustomProperty("allowEmptyOptions", true);

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			"Label 1", "Value 1");
		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			"Label 2", "Value 2");

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		deDataFieldOptions.setDEDataFieldOptions(
			Arrays.asList(deDataFieldOption1, deDataFieldOption2));

		deDataDefinitionField.setCustomProperty("value", deDataFieldOptions);

		Map<String, Object> fieldContext = new HashMap<>();

		DEOptionsFieldType optionsFieldType = new DEOptionsFieldType();

		optionsFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals(
			true, MapUtil.getBoolean(fieldContext, "allowEmptyOptions"));

		Assert.assertEquals("pt_BR", fieldContext.get("defaultLanguageId"));

		List<DEDataFieldOption> dataFieldOptionList =
			(List<DEDataFieldOption>)fieldContext.get("value");

		Assert.assertEquals(
			dataFieldOptionList.toString(), 2, dataFieldOptionList.size());

		DEDataFieldOption dataFieldOption = dataFieldOptionList.get(0);

		Assert.assertEquals(
			"Label 1",
			dataFieldOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 1", dataFieldOption.getValue());

		dataFieldOption = dataFieldOptionList.get(1);

		Assert.assertEquals(
			"Label 2",
			dataFieldOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 2", dataFieldOption.getValue());
	}

}