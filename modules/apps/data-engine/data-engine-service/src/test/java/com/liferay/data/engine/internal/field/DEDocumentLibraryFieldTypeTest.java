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
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.language.LanguageResources;
import com.liferay.portal.util.HtmlImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Marcela Cunha
 */
@PrepareForTest(
	{
		ResourceBundleLoaderUtil.class, ResourceBundleUtil.class,
		LanguageResources.class, PortalBeanLocatorUtil.class,
		AuthTokenUtil.class
	}
)
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor(
	{
		"com.liferay.portal.kernel.util.ResourceBundleLoaderUtil",
		"com.liferay.portal.kernel.security.auth.AuthTokenUtil"
	}
)
public class DEDocumentLibraryFieldTypeTest extends DEBaseFieldTypeTest {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setUpDLAppService();
		setUpFileEntry();
		setUpLanguageResources();
		setUpPortalUtil();
		setUpResourceBundle();
		setUpResourceBundleLoaderUtil();
		setUpResourceBundleUtil();
		setUpAuthTokenUtil();
	}

	@Test
	public void testIncludeContext() throws Exception {
		DEDataDefinitionField deDataDefinitionField =
			createDEDataDefinitionField("field1", "document_library");

		deDataDefinitionField.setCustomProperty("groupId", 12345);

		deDataDefinitionField.setCustomProperty(
			"value", "{\"uuid\": \"0000-1111\", \"title\": \"File Title\"}");

		Map<String, Object> fieldContext = new HashMap<>();

		ThemeDisplay themeDisplay = mock(ThemeDisplay.class);

		field(
			DEDocumentLibraryFieldType.class, "jsonFactory"
		).set(
			_deDocumentLibraryFieldType, new JSONFactoryImpl()
		);

		field(
			DEDocumentLibraryFieldType.class, "html"
		).set(
			_deDocumentLibraryFieldType, new HtmlImpl()
		);

		when(
			request.getAttribute(Matchers.anyString())
		).thenReturn(
			themeDisplay
		);

		when(
			_portal.getControlPanelPlid(Matchers.anyLong())
		).thenReturn(
			(long)1
		);

		_deDocumentLibraryFieldType.portal = _portal;

		PowerMockito.mockStatic(AuthTokenUtil.class);

		_deDocumentLibraryFieldType.includeContext(
			request, response, fieldContext, deDataDefinitionField, true);

		assertFieldProperties(fieldContext);

		Assert.assertEquals("New Title", fieldContext.get("fileEntryTitle"));

		Assert.assertTrue(fieldContext.containsKey("fileEntryURL"));

		Assert.assertEquals(12345, fieldContext.get("groupId"));

		Assert.assertTrue(fieldContext.containsKey("itemSelectorAuthToken"));

		Assert.assertTrue(fieldContext.containsKey("lexiconIconsPath"));

		Assert.assertEquals(
			"{title=File Title, uuid=0000-1111}",
			fieldContext.get(
				"value"
			).toString());

		Assert.assertTrue(fieldContext.containsKey("strings"));
	}

	protected void setUpAuthTokenUtil() {
		PowerMockito.mockStatic(AuthTokenUtil.class);

		PowerMockito.when(
			AuthTokenUtil.getToken(
				Matchers.any(HttpServletRequest.class), Matchers.anyLong(),
				Matchers.anyString())
		).thenReturn(
			"test"
		);
	}

	protected void setUpDLAppService() throws Exception {
		_deDocumentLibraryFieldType.dlAppService = _dlAppService;

		when(
			_dlAppService.getFileEntryByUuidAndGroupId(
				Matchers.anyString(), Matchers.anyLong())
		).thenReturn(
			_fileEntry
		);
	}

	protected void setUpFileEntry() {
		_fileEntry.setUuid("0000-1111");
		_fileEntry.setGroupId(12345);

		when(
			_fileEntry.getTitle()
		).thenReturn(
			"New Title"
		);
	}

	protected void setUpLanguageResources() {
		PowerMockito.mockStatic(LanguageResources.class);

		ResourceBundle resourceBundle = mock(ResourceBundle.class);

		when(
			LanguageResources.getResourceBundle(locale)
		).thenReturn(
			resourceBundle
		);
	}

	protected void setUpPortalUtil() throws Exception {
		PortalUtil portalUtil = new PortalUtil();

		ResourceBundle resourceBundle = mock(ResourceBundle.class);

		when(
			_portal.getResourceBundle(Matchers.any(Locale.class))
		).thenReturn(
			resourceBundle
		);

		portalUtil.setPortal(_portal);
	}

	protected void setUpResourceBundle() {
		ResourceBundle resourceBundle = mock(ResourceBundle.class);

		when(
			_portal.getResourceBundle(Matchers.any(Locale.class))
		).thenReturn(
			resourceBundle
		);
	}

	protected void setUpResourceBundleLoaderUtil() {
		PowerMockito.mockStatic(ResourceBundleLoaderUtil.class);

		ResourceBundleLoader portalResourceBundleLoader = mock(
			ResourceBundleLoader.class);

		when(
			ResourceBundleLoaderUtil.getPortalResourceBundleLoader()
		).thenReturn(
			portalResourceBundleLoader
		);
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

	protected void whenLanguageGet(
		Language language, Locale locale, String key, String returnValue) {

		when(
			language.get(Matchers.any(ResourceBundle.class), Matchers.eq(key))
		).thenReturn(
			returnValue
		);
	}

	private final DEDocumentLibraryFieldType _deDocumentLibraryFieldType =
		new DEDocumentLibraryFieldType();

	@Mock
	private DLAppService _dlAppService;

	@Mock
	private FileEntry _fileEntry;

	@Mock
	private Portal _portal;

}