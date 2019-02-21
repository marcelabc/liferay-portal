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
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@PrepareForTest(ResourceBundleUtil.class)
@RunWith(PowerMockRunner.class)
public class DESelectFieldTypeTest extends DEBaseFieldTypeTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();

		setUpResourceBundleUtil();
	}

	@Test
	public void testIncludeContext() throws Exception {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "select");

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "[\"1\"]");
					put("pt_BR", "[\"1\"]");
				}
			});

		deDataDefinitionField.setCustomProperty("multiple", true);
		deDataDefinitionField.setCustomProperty("dataSourceType", "manual");

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			"Label 1", "Value 1");
		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			"Label 2", "Value 2");

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		deDataFieldOptions.setDEDataFieldOptions(
			Arrays.asList(deDataFieldOption1, deDataFieldOption2));

		deDataDefinitionField.setCustomProperty("options", deDataFieldOptions);

		deDataDefinitionField.setCustomProperty("value", "[\"1\"]");

		Map<String, Object> fieldContext = new HashMap<>();

		DESelectFieldType selectFieldType = new DESelectFieldType();

		field(
			DESelectFieldType.class, "jsonFactory"
		).set(
			selectFieldType, new JSONFactoryImpl()
		);

		selectFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals(true, MapUtil.getBoolean(fieldContext, "multiple"));
		Assert.assertEquals(
			"manual", MapUtil.getString(fieldContext, "dataSourceType"));

		List<String> stringList = (List<String>)fieldContext.get(
			"predefinedValue");

		Assert.assertArrayEquals(
			new String[] {"1"}, ArrayUtil.toStringArray(stringList));

		stringList = (List<String>)fieldContext.get("value");

		Assert.assertArrayEquals(
			new String[] {"1"}, ArrayUtil.toStringArray(stringList));

		List<DEDataFieldOption> dataFieldOptions =
			(List<DEDataFieldOption>)fieldContext.get("options");

		Assert.assertEquals(
			dataFieldOptions.toString(), 2, dataFieldOptions.size());

		DEDataFieldOption dataFieldOption = dataFieldOptions.get(0);

		Assert.assertEquals(
			"Label 1",
			dataFieldOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 1", dataFieldOption.getValue());

		dataFieldOption = dataFieldOptions.get(1);

		Assert.assertEquals(
			"Label 2",
			dataFieldOption.getLabel(language.getLanguageId(locale)));
		Assert.assertEquals("Value 2", dataFieldOption.getValue());
	}

	protected void setUpResourceBundleUtil() {
		PowerMockito.mockStatic(ResourceBundleUtil.class);

		PowerMockito.when(
			ResourceBundleUtil.getBundle(
				Matchers.anyString(), Matchers.any(Locale.class),
				Matchers.any(ClassLoader.class))
		).thenReturn(
			ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE
		);
	}

	@Mock
	private Portal _portal;

	@Mock
	private ResourceBundle _resourceBundle;

}