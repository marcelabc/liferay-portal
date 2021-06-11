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

package com.liferay.data.engine.taglib.internal.servlet.taglib.util;

import com.liferay.data.engine.rest.internal.resource.v2_0.DataDefinitionResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.factory.DataDefinitionResourceFactoryImpl;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcela Cunha
 */
public class DataLayoutTaglibUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		ReflectionTestUtil.setFieldValue(
			_dataLayoutTaglibUtil, "_dataLayoutTaglibUtil",
			_dataLayoutTaglibUtil);

		ReflectionTestUtil.setFieldValue(
			_dataLayoutTaglibUtil, "_jsonFactory", new JSONFactoryImpl());

		ReflectionTestUtil.setFieldValue(
			_dataLayoutTaglibUtil, "_dataDefinitionResourceFactory",
			_dataDefinitionResourceFactory);

		ReflectionTestUtil.setFieldValue(
			_dataLayoutTaglibUtil, "_portal", _portal);

		when(
			_dataDefinitionResourceFactory.create()
		).thenReturn(
			_dataDefinitionResourceBuilder
		);

		when(
			_portal.getUser(_httpServletRequest)
		).thenReturn(
			mock(User.class)
		);

		when(
			_dataDefinitionResourceBuilder.httpServletRequest(_httpServletRequest)
		).thenReturn(
			_dataDefinitionResourceBuilder2
		);

		when(
			_dataDefinitionResourceBuilder2.user(_portal.getUser(_httpServletRequest))
		).thenReturn(
			_dataDefinitionResourceBuilder3
		);

		when(
			_dataDefinitionResourceBuilder3.build()
		).thenReturn(
			_dataDefinitionResource
		);

	}

	@Test
	public void test() throws Exception {
		_dataLayoutTaglibUtil.getFieldTypesJSONArray(
			_httpServletRequest, Collections.singleton(_JOURNAL), false);
	}

	private static final String _JOURNAL = "journal";

	private final DataLayoutTaglibUtil _dataLayoutTaglibUtil =
		new DataLayoutTaglibUtil();
	private HttpServletRequest _httpServletRequest = mock(
		HttpServletRequest.class);

	private Portal _portal = mock(Portal.class);

	private DataDefinitionResource.Factory _dataDefinitionResourceFactory = mock(DataDefinitionResource.Factory.class);

	private DataDefinitionResource.Builder _dataDefinitionResourceBuilder = mock(DataDefinitionResource.Builder.class);

	private DataDefinitionResource.Builder _dataDefinitionResourceBuilder2 = mock(DataDefinitionResource.Builder.class);
	private DataDefinitionResource.Builder _dataDefinitionResourceBuilder3 = mock(DataDefinitionResource.Builder.class);

	private DataDefinitionResource _dataDefinitionResource = new DataDefinitionResourceImpl();
}