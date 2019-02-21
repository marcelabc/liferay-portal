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

package com.liferay.data.engine.renderer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataFieldOption;
import com.liferay.data.engine.model.DEDataFieldOptions;
import com.liferay.data.engine.model.DEDataLayout;
import com.liferay.data.engine.model.DEDataLayoutColumn;
import com.liferay.data.engine.model.DEDataLayoutPage;
import com.liferay.data.engine.model.DEDataLayoutRow;
import com.liferay.data.engine.renderer.DEDataLayoutRenderer;
import com.liferay.data.engine.service.DEDataDefinitionRequestBuilder;
import com.liferay.data.engine.service.DEDataDefinitionSaveRequest;
import com.liferay.data.engine.service.DEDataDefinitionSaveResponse;
import com.liferay.data.engine.service.DEDataDefinitionService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Leonardo Barros
 */
@RunWith(Arquillian.class)
public class DEDataLayoutRendererTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addOmniAdminUser();

		_setUpHttpServletRequest();

		_httpServletResponse = new MockHttpServletResponse();
	}

	@Test
	public void testRender() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_user));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, _user.getGroupId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataDefinitionField deDataDefinitionField1 = _createTextField(
				"field1");

			DEDataDefinitionField deDataDefinitionField2 = _createCheckboxField(
				"field2");

			DEDataDefinitionField deDataDefinitionField3 =
				_createMultipleCheckboxField("field3");

			DEDataDefinitionField deDataDefinitionField4 = _createDateField(
				"field4");

			DEDataDefinitionField deDataDefinitionField5 = _createEditorField(
				"field5");

			DEDataDefinitionField deDataDefinitionField6 = _createGridField(
				"field6");

			DEDataDefinitionField deDataDefinitionField7 = _createKeyValueField(
				"field7");

			DEDataDefinitionField deDataDefinitionField8 = _createNumericField(
				"field8");

			DEDataDefinitionField deDataDefinitionField9 = _createOptionsField(
				"field9");

			DEDataDefinitionField deDataDefinitionField10 =
				_createParagraphField("field10");

			DEDataDefinitionField deDataDefinitionField11 =
				_createPasswordField("field11");

			DEDataDefinitionField deDataDefinitionField12 = _createRadioField(
				"field12");

			DEDataDefinitionField deDataDefinitionField13 = _createSelectField(
				"field13");

			DEDataDefinition deDataDefinition = new DEDataDefinition();

			deDataDefinition.addName(LocaleUtil.US, "Definition Test");

			deDataDefinition.setDEDataDefinitionFields(
				Arrays.asList(
					deDataDefinitionField1, deDataDefinitionField2,
					deDataDefinitionField3, deDataDefinitionField4,
					deDataDefinitionField5, deDataDefinitionField6,
					deDataDefinitionField7, deDataDefinitionField8,
					deDataDefinitionField9, deDataDefinitionField10,
					deDataDefinitionField11, deDataDefinitionField12,
					deDataDefinitionField13));

			deDataDefinition.setStorageType("json");

			DEDataDefinitionSaveRequest deDataDefinitionSaveRequest =
				DEDataDefinitionRequestBuilder.saveBuilder(
					deDataDefinition
				).inGroup(
					_group.getGroupId()
				).onBehalfOf(
					_user.getUserId()
				).build();

			DEDataDefinitionSaveResponse deDataDefinitionSaveResponse =
				_deDataDefinitionService.execute(deDataDefinitionSaveRequest);

			DEDataDefinition deDataDefinitionSaved =
				deDataDefinitionSaveResponse.getDEDataDefinition();

			deDataDefinition.setDEDataDefinitionId(
				deDataDefinitionSaved.getDEDataDefinitionId());

			DEDataLayoutColumn deDataLayoutColumn1 = new DEDataLayoutColumn();

			deDataLayoutColumn1.setColumnSize(12);
			deDataLayoutColumn1.setFieldsName(
				Arrays.asList("field1", "field2", "field3"));

			DEDataLayoutColumn deDataLayoutColumn2 = new DEDataLayoutColumn();

			deDataLayoutColumn2.setColumnSize(12);
			deDataLayoutColumn2.setFieldsName(
				Arrays.asList("field4", "field5", "field6"));

			DEDataLayoutColumn deDataLayoutColumn3 = new DEDataLayoutColumn();

			deDataLayoutColumn3.setColumnSize(12);
			deDataLayoutColumn3.setFieldsName(
				Arrays.asList("field7", "field8", "field9"));

			DEDataLayoutColumn deDataLayoutColumn4 = new DEDataLayoutColumn();

			deDataLayoutColumn4.setColumnSize(12);
			deDataLayoutColumn4.setFieldsName(
				Arrays.asList("field10", "field11", "field12"));

			DEDataLayoutColumn deDataLayoutColumn5 = new DEDataLayoutColumn();

			deDataLayoutColumn5.setColumnSize(12);
			deDataLayoutColumn5.setFieldsName(Arrays.asList("field13"));

			Queue<DEDataLayoutColumn> deDataLayoutColumns = new ArrayDeque<>();

			deDataLayoutColumns.add(deDataLayoutColumn1);
			deDataLayoutColumns.add(deDataLayoutColumn2);
			deDataLayoutColumns.add(deDataLayoutColumn3);
			deDataLayoutColumns.add(deDataLayoutColumn4);
			deDataLayoutColumns.add(deDataLayoutColumn5);

			DEDataLayoutRow deDataLayoutRow = new DEDataLayoutRow();

			deDataLayoutRow.setDEDataLayoutColumns(deDataLayoutColumns);

			Queue<DEDataLayoutRow> deDataLayoutRows = new ArrayDeque<>();

			deDataLayoutRows.add(deDataLayoutRow);

			DEDataLayoutPage deDataLayoutPage = new DEDataLayoutPage();

			deDataLayoutPage.setTitle(
				new HashMap() {
					{
						put("en_US", "Page");
					}
				});
			deDataLayoutPage.setDescription(
				new HashMap() {
					{
						put("en_US", StringPool.BLANK);
					}
				});
			deDataLayoutPage.setDEDataLayoutRows(deDataLayoutRows);

			Queue<DEDataLayoutPage> deDataLayoutPages = new ArrayDeque<>();

			deDataLayoutPages.add(deDataLayoutPage);

			DEDataLayout deDataLayout = new DEDataLayout();

			deDataLayout.setName(
				new HashMap() {
					{
						put(LocaleUtil.US, "layout");
					}
				});
			deDataLayout.setDescription(
				new HashMap() {
					{
						put(LocaleUtil.US, "this is a layout");
					}
				});
			deDataLayout.setDEDataLayoutPages(deDataLayoutPages);
			deDataLayout.setPaginationMode("wizard");
			deDataLayout.setDefaultLanguageId("en_US");

			deDataLayout.setDEDataDefinition(deDataDefinition);

			String html = _deDataLayoutRenderer.render(
				_httpServletRequest, _httpServletResponse, deDataLayout, false);

			Assert.assertNotNull(html);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	private DEDataDefinitionField _createCheckboxField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "checkbox");

		deDataDefinitionField.setCustomProperty("showAsSwitcher", true);
		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", true);
					put("pt_BR", true);
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createDateField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "date");

		deDataDefinitionField.addLabels(
			new HashMap() {
				{
					put("en_US", "Date Label");
					put("pt_BR", "Date Label BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "01/01/2019");
					put("pt_BR", "01/01/2019");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"tooltip",
			new HashMap() {
				{
					put("en_US", "Text Tooltip");
					put("pt_BR", "Text Tooltip BR");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createEditorField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "editor");

		deDataDefinitionField.setCustomProperty(
			"placeholder",
			new HashMap() {
				{
					put("en_US", "Editor Placeholder");
					put("pt_BR", "Editor Placeholder BR");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createGridField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "grid");

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Row 1");
					put("pt_BR", "Linha 1");
				}
			},
			"1");

		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Row 2");
					put("pt_BR", "Linha 2");
				}
			},
			"2");

		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("rows", deDataFieldOptions);

		deDataFieldOptions = new DEDataFieldOptions();

		deDataFieldOptionList = new ArrayList<>();

		deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Column 1");
					put("pt_BR", "Coluna 1");
				}
			},
			"1");

		deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Column 2");
					put("pt_BR", "Coluna 2");
				}
			},
			"2");
		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("columns", deDataFieldOptions);

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createKeyValueField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "key_value");

		deDataDefinitionField.setCustomProperty("autoFocus", true);

		deDataDefinitionField.setCustomProperty(
			"placeholder",
			new HashMap() {
				{
					put("en_US", "Key Value Placeholder");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"tooltip",
			new HashMap() {
				{
					put("en_US", "Key Value Tooltip");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createMultipleCheckboxField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "checkbox_multiple");

		deDataDefinitionField.setCustomProperty("showAsSwitcher", true);
		deDataDefinitionField.setCustomProperty("inline", true);

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 1");
					put("pt_BR", "Rótulo 1");
				}
			},
			"1");

		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 2");
					put("pt_BR", "Rótulo 2");
				}
			},
			"2");

		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("options", deDataFieldOptions);

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "[\"1\",\"2\"]");
					put("pt_BR", "[\"1\",\"2\"]");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createNumericField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "numeric");

		deDataDefinitionField.addLabels(
			new HashMap() {
				{
					put("en_US", "Numeric Default");
					put("pt_BR", "Numeric Default BR");
				}
			});
		deDataDefinitionField.setCustomProperty("dataType", "decimal");
		deDataDefinitionField.setCustomProperty(
			"placeholder",
			new HashMap() {
				{
					put("en_US", "Numeric Placeholder");
					put("pt_BR", "Numeric Placeholder BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"tooltip",
			new HashMap() {
				{
					put("en_US", "Numeric Tooltip");
					put("pt_BR", "Numeric Tooltip BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", 1);
					put("pt_BR", 1);
				}
			});

		deDataDefinitionField.setCustomProperty("value", 2.5);

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createOptionsField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "options");

		deDataDefinitionField.setCustomProperty("allowEmptyOptions", true);

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 1");
					put("pt_BR", "Rótulo 1");
				}
			},
			"1");

		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 2");
					put("pt_BR", "Rótulo 2");
				}
			},
			"2");

		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("value", deDataFieldOptions);

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createParagraphField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "paragraph");

		deDataDefinitionField.setCustomProperty(
			"text",
			new HashMap() {
				{
					put("en_US", "<b>Text</b>");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createPasswordField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "password");

		deDataDefinitionField.addLabels(
			new HashMap() {
				{
					put("en_US", "Password Label");
				}
			});
		deDataDefinitionField.setCustomProperty(
			"placeholder",
			new HashMap() {
				{
					put("en_US", "Password Placeholder");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"tooltip",
			new HashMap() {
				{
					put("en_US", "Password Tooltip");
				}
			});

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createRadioField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "radio");

		deDataDefinitionField.setCustomProperty("inline", true);

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 1");
					put("pt_BR", "Rótulo 1");
				}
			},
			"1");

		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 2");
					put("pt_BR", "Rótulo 2");
				}
			},
			"2");

		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("options", deDataFieldOptions);

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "[\"1\"]");
					put("pt_BR", "[\"1\"]");
				}
			});

		deDataDefinitionField.setCustomProperty("value", "[\"1\"]");

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createSelectField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "select");

		deDataDefinitionField.setCustomProperty("dataSourceType", "manual");
		deDataDefinitionField.setCustomProperty("multiple", true);

		DEDataFieldOptions deDataFieldOptions = new DEDataFieldOptions();

		List<DEDataFieldOption> deDataFieldOptionList = new ArrayList<>();

		DEDataFieldOption deDataFieldOption1 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 1");
					put("pt_BR", "Rótulo 1");
				}
			},
			"1");

		DEDataFieldOption deDataFieldOption2 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 2");
					put("pt_BR", "Rótulo 2");
				}
			},
			"2");

		DEDataFieldOption deDataFieldOption3 = new DEDataFieldOption(
			new HashMap() {
				{
					put("en_US", "Label 3");
					put("pt_BR", "Rótulo 3");
				}
			},
			"3");

		deDataFieldOptionList.add(deDataFieldOption1);
		deDataFieldOptionList.add(deDataFieldOption2);
		deDataFieldOptionList.add(deDataFieldOption3);

		deDataFieldOptions.setDEDataFieldOptions(deDataFieldOptionList);

		deDataDefinitionField.setCustomProperty("options", deDataFieldOptions);

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "[\"1\"]");
					put("pt_BR", "[\"1\"]");
				}
			});

		deDataDefinitionField.setCustomProperty("value", "[\"1\", \"2\"]");

		return deDataDefinitionField;
	}

	private DEDataDefinitionField _createTextField(String name) {
		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			name, "text");

		deDataDefinitionField.setCustomProperty("displayStyle", "singleline");

		deDataDefinitionField.addLabels(
			new HashMap() {
				{
					put("en_US", "Text Label");
					put("pt_BR", "Text Label BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"placeholder",
			new HashMap() {
				{
					put("en_US", "Text Placeholder");
					put("pt_BR", "Text Placeholder BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"predefinedValue",
			new HashMap() {
				{
					put("en_US", "Simple Text");
					put("pt_BR", "Simple Text BR");
				}
			});

		deDataDefinitionField.setCustomProperty(
			"tooltip",
			new HashMap() {
				{
					put("en_US", "Text Tooltip");
					put("pt_BR", "Text Tooltip BR");
				}
			});

		return deDataDefinitionField;
	}

	private void _setUpHttpServletRequest() throws PortalException {
		_httpServletRequest = new MockHttpServletRequest();

		((MockHttpServletRequest)_httpServletRequest).addPreferredLocale(
			LocaleUtil.US);

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(_group.getCompanyId()));
		themeDisplay.setPathThemeImages(StringPool.BLANK);
		themeDisplay.setRealUser(_user);
		themeDisplay.setUser(_user);

		_httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
	}

	@Inject(type = CompanyLocalService.class)
	private CompanyLocalService _companyLocalService;

	@Inject(type = DEDataDefinitionService.class)
	private DEDataDefinitionService _deDataDefinitionService;

	@Inject(type = DEDataLayoutRenderer.class)
	private DEDataLayoutRenderer _deDataLayoutRenderer;

	@DeleteAfterTestRun
	private Group _group;

	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;

	@DeleteAfterTestRun
	private User _user;

}