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
import com.liferay.data.engine.rest.resource.v1_0.test.util.DataDefinitionTestUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.vulcan.pagination.Pagination;

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

	@Test
	public void testSearchDataDefinitionDataLayoutsByBlankName()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout1 =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("form layout"));

		DataLayout dataLayout2 =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("app layout"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "", Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout1, dataLayout2),
			(List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByFullName()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("form layout"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "form layout", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByLongName()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId,
				randomDataLayout("abcdefghijklmnopqrstuvwxyz0123456789"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "abcdefghijklmnopqrstuvwxyz0123456789",
			Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByNameWithNonasciiChar()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("π€† layout"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "π€† layout", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByNameWithSpecialASCIIChar()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("!@#layout"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "!@#l", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataDefinitionDataLayoutsByPartialName()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		DataLayout dataLayout =
			testGetDataDefinitionDataLayoutsPage_addDataLayout(
				dataDefinitionId, randomDataLayout("form layout"));

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "layo", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByBlankName() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout1 = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("form layout"));

		DataLayout dataLayout2 = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("app layout"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, " ", Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());
		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout1, dataLayout2),
			(List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByCaseSensitiveName() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("FoRmSLaYoUt"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "FORM", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());
		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByFullName() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("form layout"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "form layout", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByLongName() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("abcdefghijklmnopqrstuvwxyz0123456789"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "abcdefghijklmnopqrstuvwxyz0123456789",
			Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());
		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByNameWithNonasciiChar() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("π€† layout"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "π€†", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByNameWithSpecialASCIIChar()
		throws Exception {

		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("!@#layout "));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "!@#l", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchDataLayoutByPartialName() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout("form layout"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "layo", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testSearchNonexistingDataDefinitionDataLayouts()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataLayoutsPage_getDataDefinitionId();

		Page<DataLayout> page = invokeGetDataDefinitionDataLayoutsPage(
			dataDefinitionId, "layout", Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());
	}

	@Test
	public void testSearchNonexistingDataLayout() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, "form layout", Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());
	}

	@Test
	public void testSearchSiteDataLayoutPage() throws Exception {
		Long siteId = testGetSiteDataLayoutPage_getSiteId();

		DataLayout dataLayout = testGetSiteDataLayoutPage_addDataLayout(
			siteId, randomDataLayout(" article layout"));

		Page<DataLayout> page = invokeGetSiteDataLayoutPage(
			siteId, " arti", Pagination.of(1, 2));

		Assert.assertEquals(1, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataLayout), (List<DataLayout>)page.getItems());
		assertValid(page);
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"dataDefinitionId", "name"};
	}

	@Override
	protected DataLayout randomDataLayout() {
		return new DataLayout() {
			{
				dataDefinitionId = _ddmStructure.getStructureId();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				defaultLanguageId = "en_US";
				id = RandomTestUtil.randomLong();
				name = new HashMap<String, Object>() {
					{
						put("en_US", RandomTestUtil.randomString());
					}
				};
			}
		};
	}

	protected DataLayout randomDataLayout(String value) {
		return new DataLayout() {
			{
				dataDefinitionId = _ddmStructure.getStructureId();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				defaultLanguageId = "en_US";
				id = RandomTestUtil.randomLong();
				name = new HashMap<String, Object>() {
					{
						put("en_US", value);
					}
				};
			}
		};
	}

	@Override
	protected DataLayout randomIrrelevantDataLayout() {
		DataLayout dataLayout = randomDataLayout();

		dataLayout.setDataDefinitionId(
			_irrelevantDDMStructure.getStructureId());

		return dataLayout;
	}

	@Override
	protected DataLayout testDeleteDataLayout_addDataLayout() throws Exception {
		return invokePostDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	@Override
	protected Long testGetDataDefinitionDataLayoutsPage_getDataDefinitionId()
		throws Exception {

		return _ddmStructure.getStructureId();
	}

	@Override
	protected DataLayout testGetDataLayout_addDataLayout() throws Exception {
		return invokePostDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	@Override
	protected DataLayout testGetSiteDataLayoutPage_addDataLayout(
			Long siteId, DataLayout dataLayout)
		throws Exception {

		return invokePostDataDefinitionDataLayout(
			dataLayout.getDataDefinitionId(), dataLayout);
	}

	@Override
	protected DataLayout testPutDataLayout_addDataLayout() throws Exception {
		return invokePostDataDefinitionDataLayout(
			_ddmStructure.getStructureId(), randomDataLayout());
	}

	private DDMStructure _ddmStructure;
	private DDMStructure _irrelevantDDMStructure;

}