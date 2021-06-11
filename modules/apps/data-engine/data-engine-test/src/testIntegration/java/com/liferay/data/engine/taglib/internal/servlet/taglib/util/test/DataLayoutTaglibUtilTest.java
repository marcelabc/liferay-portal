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

package com.liferay.data.engine.taglib.internal.servlet.taglib.util.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.taglib.internal.servlet.taglib.util.DataLayoutTaglibUtil;
import com.liferay.portal.test.rule.Inject;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Marcela Cunha
 */
@RunWith(Arquillian.class)
public class DataLayoutTaglibUtilTest {

	@Before
	public void setUp() throws Exception {
		_httpServletRequest = new MockHttpServletRequest();
	}

	@Test
	public void testGetFieldTypesJSONArrayWithSearchableFieldsDisabled()
		throws Exception {

		_dataLayoutTaglibUtil.getFieldTypesJSONArray(
			_httpServletRequest, Collections.singleton(_JOURNAL), false);
	}

	private static final String _JOURNAL = "journal";

	@Inject
	private DataLayoutTaglibUtil _dataLayoutTaglibUtil;

	private HttpServletRequest _httpServletRequest;

}