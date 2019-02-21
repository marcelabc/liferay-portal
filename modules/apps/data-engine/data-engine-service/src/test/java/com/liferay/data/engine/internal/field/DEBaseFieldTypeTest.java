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
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageConstants;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Leonardo Barros
 */
public class DEBaseFieldTypeTest extends PowerMockito {

	@BeforeClass
	public static void setUpClass() throws Exception {
		PropsUtil.setProps(new PropsImpl());
	}

	@Before
	public void setUp() throws Exception {
		setUpLanguageUtil();
	}

	protected void assertFieldProperties(Map<String, Object> fieldProperties) {
		Assert.assertEquals(
			"Label BR", MapUtil.getString(fieldProperties, "label"));
		Assert.assertEquals(
			"field1", MapUtil.getString(fieldProperties, "name"));
		Assert.assertEquals(
			true, MapUtil.getBoolean(fieldProperties, "readOnly"));
		Assert.assertEquals(
			true, MapUtil.getBoolean(fieldProperties, "required"));
		Assert.assertEquals(
			true, MapUtil.getBoolean(fieldProperties, "showLabel"));
		Assert.assertEquals(
			"Tip BR", MapUtil.getString(fieldProperties, "tip"));
		Assert.assertEquals(
			true, MapUtil.getBoolean(fieldProperties, "visible"));
	}

	protected DEDataDefinitionField createDEDataDefinitionField(
		String name, String type) {

		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, type);

		Map<String, String> label = new HashMap() {
			{
				put("en_US", "Label US");
				put("pt_BR", "Label BR");
			}
		};

		deDataDefinitionField.setLabel(label);

		Map<String, String> tip = new HashMap() {
			{
				put("en_US", "Tip US");
				put("pt_BR", "Tip BR");
			}
		};

		deDataDefinitionField.setTip(tip);

		deDataDefinitionField.setCustomProperty("readOnly", true);
		deDataDefinitionField.setCustomProperty("required", true);
		deDataDefinitionField.setCustomProperty("showLabel", true);
		deDataDefinitionField.setCustomProperty("visible", true);

		return deDataDefinitionField;
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(language);

		when(
			language.getLanguageId(locale)
		).thenReturn(
			"pt_BR"
		);

		when(
			language.get(locale, LanguageConstants.KEY_DIR)
		).thenReturn(
			"left"
		);

		when(
			language.getLanguageId(Matchers.any(HttpServletRequest.class))
		).thenReturn(
			"pt_BR"
		);
	}

	@Mock
	protected Language language;

	protected final Locale locale = new Locale("pt", "BR");

	@Mock
	protected HttpServletRequest request;

	@Mock
	protected HttpServletResponse response;

}