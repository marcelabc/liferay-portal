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

package com.liferay.data.engine.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.rest.client.dto.v1_0.DataLayout;
import com.liferay.data.engine.rest.client.pagination.Page;
import com.liferay.data.engine.rest.client.pagination.Pagination;
import com.liferay.data.engine.rest.resource.v1_0.test.util.DataDefinitionTestUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcelo Mello
 */
@RunWith(Arquillian.class)
public class DataLayoutResourceTest extends BaseDataLayoutResourceTestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();

		_ddmStructure = DataDefinitionTestUtil.addDDMStructure(testGroup);
		_irrelevantDDMStructure = DataDefinitionTestUtil.addDDMStructure(
			irrelevantGroup);
	}

	@Override
	@Test
	public void testPostDataDefinitionDataLayout() throws Exception {
		super.testPostDataDefinitionDataLayout();

		// Multiple data layouts with the same data definition

		DataLayout randomDataLayout = randomDataLayout();

		for (int i = 0; i < 3; i++) {
			DataLayout postDataLayout =
				testPostDataDefinitionDataLayout_addDataLayout(
					randomDataLayout);

			assertEquals(randomDataLayout, postDataLayout);
			assertValid(postDataLayout);
		}
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByFullName()
		throws Exception {

		searchDataDefinitionDataLayout("form layout", "form layout");
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByLongName()
		throws Exception {

		searchDataDefinitionDataLayout(
			"abcdefghijklmnopqrstuvwxyz0123456789",
			"abcdefghijklmnopqrstuvwxyz0123456789");
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByNameWithNonasciiChar()
		throws Exception {

		searchDataDefinitionDataLayout("π€† layout", "π€† layout");
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByNameWithSpecialASCIIChar()
		throws Exception {

		searchDataDefinitionDataLayout("!@#layout", "!@#l");
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByPartialName()
		throws Exception {

		searchDataDefinitionDataLayout("form layout", "layo");
	}

	@Test
	public void testSearchDataLayoutByCaseSensitiveName() throws Exception {
		searchSiteDataLayout("FoRmSLaYoUt", "FORM");
	}

	@Test
	public void testSearchDataLayoutByFullName() throws Exception {
		searchSiteDataLayout("form layout", "form layout");
	}

	@Test
	public void testSearchDataLayoutByLongName() throws Exception {
		searchSiteDataLayout(
			"abcdefghijklmnopqrstuvwxyz0123456789",
			"abcdefghijklmnopqrstuvwxyz0123456789");
	}

	@Test
	public void testSearchDataLayoutByNameWithNonasciiChar() throws Exception {
		searchSiteDataLayout("π€† layout", "π€†");
	}

	@Test
	public void testSearchDataLayoutByNameWithSpecialASCIIChar()
		throws Exception {

		searchSiteDataLayout("!@#layout", "!@#l");
	}

	@Test
	public void testSearchDataLayoutByPartialName() throws Exception {
		searchSiteDataLayout("form layout", "layo");
	}

	@Test
	public void testSearchNonexistingDataDefinitionDataLayouts()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		Page<DataLayout> page =
			dataLayoutResource.getDataDefinitionDataLayoutsPage(
				dataDefinitionId, "layout", Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());
	}

	@Test
	public void testSearchNonexistingDataLayout() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		Page<DataLayout> page = dataLayoutResource.getSiteDataLayoutPage(
			siteId, "form layout", Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());
	}

	@Test
	public void testSearchSiteDataLayoutPage() throws Exception {
		searchSiteDataLayout("article layout", "arti");
	}

	protected DataLayout createDataLayout(String dataLayoutName) {
		return new DataLayout() {
			{
				dataDefinitionId = _ddmStructure.getStructureId();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				defaultLanguageId = "en_US";
				id = RandomTestUtil.randomLong();
				name = new HashMap<String, Object>() {
					{
						put("en_US", dataLayoutName);
					}
				};
			}
		};
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"dataDefinitionId", "name"};
	}

	@Override
	protected DataLayout randomDataLayout() {
		return createDataLayout(RandomTestUtil.randomString());
	}

	@Override
	protected DataLayout randomIrrelevantDataLayout() {
		DataLayout dataLayout = randomDataLayout();

		dataLayout.setDataDefinitionId(
			_irrelevantDDMStructure.getStructureId());

		return dataLayout;
	}

	protected void searchDataDefinitionDataLayout(
			String dataLayoutName, String keywords)
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, createDataLayout(dataLayoutName));

		Page<DataLayout> page =
			dataLayoutResource.getDataDefinitionDataLayoutsPage(
				dataDefinitionId, keywords, Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	protected void searchSiteDataLayout(String dataLayoutName, String keywords)
		throws Exception {

		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, createDataLayout(dataLayoutName));

		Page<DataLayout> page = dataLayoutResource.getSiteDataLayoutPage(
			siteId, keywords, Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Override
	protected DataLayout testDeleteDataLayout_addDataLayout() throws Exception {
		return dataLayoutResource.postDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	@Override
	protected Long testGetDataDefinitionDataLayoutsPage_getDataDefinitionId()
		throws Exception {

		return _ddmStructure.getStructureId();
	}

	@Override
	protected DataLayout testGetDataLayout_addDataLayout() throws Exception {
		return dataLayoutResource.postDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	@Override
	protected DataLayout testGetSiteDataLayoutPage_addDataLayout(
			Long siteId, DataLayout dataLayout)
		throws Exception {

		return dataLayoutResource.postDataDefinitionDataLayout(
			dataLayout.getDataDefinitionId(), dataLayout);
	}

	@Override
	protected DataLayout testPutDataLayout_addDataLayout() throws Exception {
		return dataLayoutResource.postDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	private DDMStructure _ddmStructure;
	private DDMStructure _irrelevantDDMStructure;

}