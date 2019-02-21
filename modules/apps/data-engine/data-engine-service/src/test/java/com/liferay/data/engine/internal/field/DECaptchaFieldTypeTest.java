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
import com.liferay.portal.template.soy.data.SoyDataFactory;
import com.liferay.portal.template.soy.data.SoyHTMLData;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class DECaptchaFieldTypeTest extends DEBaseFieldTypeTest {

	@Test
	public void testIncludeContext() {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "captcha");

		Map<String, Object> fieldContext = new HashMap<>();

		DECaptchaFieldType captchaFieldType = new DECaptchaFieldType();

		when(
			_soyDataFactory.createSoyHTMLData(Matchers.anyString())
		).thenReturn(
			_soyHTMLData
		);

		captchaFieldType.soyDataFactory = _soyDataFactory;

		captchaFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals(_soyHTMLData, fieldContext.get("html"));
	}

	@Mock
	private SoyDataFactory _soyDataFactory;

	@Mock
	private SoyHTMLData _soyHTMLData;

}