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

package com.liferay.data.engine.internal.renderer;

import com.liferay.data.engine.exception.DEDataLayoutRendererException;
import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.internal.field.DEFieldTypeTracker;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataLayout;
import com.liferay.data.engine.model.DEDataLayoutColumn;
import com.liferay.data.engine.model.DEDataLayoutPage;
import com.liferay.data.engine.model.DEDataLayoutRow;
import com.liferay.data.engine.renderer.DEDataLayoutRenderer;
import com.liferay.data.engine.util.DEDataEngineUtil;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.template.soy.renderer.ComponentDescriptor;
import com.liferay.portal.template.soy.renderer.SoyComponentRenderer;

import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true, service = DEDataLayoutRenderer.class)
public class DEDataLayoutRendererImpl implements DEDataLayoutRenderer {

	@Override
	public String render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, DEDataLayout deDataLayout,
			boolean readOnly)
		throws DEDataLayoutRendererException {

		Writer writer = new UnsyncStringWriter();

		ComponentDescriptor componentDescriptor = new ComponentDescriptor(
			_TEMPLATE_NAMESPACE, npmResolver.resolveModuleName(_MODULE_NAME));

		try {
			Map<String, Object> context = new HashMap<>();

			includeContext(
				httpServletRequest, httpServletResponse, context, deDataLayout,
				readOnly);

			soyComponentRenderer.renderSoyComponent(
				httpServletRequest, writer, componentDescriptor, context);
		}
		catch (Exception e) {
			throw new DEDataLayoutRendererException(e);
		}

		return writer.toString();
	}

	protected List<Object> createColumnsTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		Queue<DEDataLayoutColumn> deDataLayoutColumns, boolean readOnly) {

		List<Object> columnsContext = new ArrayList<>();

		for (DEDataLayoutColumn deDataLayoutColumn : deDataLayoutColumns) {
			columnsContext.add(
				createColumnTemplateContext(
					httpServletRequest, httpServletResponse,
					deDataDefinitionFields, deDataLayoutColumn, readOnly));
		}

		return columnsContext;
	}

	protected Map<String, Object> createColumnTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		DEDataLayoutColumn deDataLayoutColumn, boolean readOnly) {

		Map<String, Object> columnTemplateContext = new HashMap<>();

		columnTemplateContext.put(
			"fields",
			createFieldsTemplateContext(
				httpServletRequest, httpServletResponse, deDataDefinitionFields,
				deDataLayoutColumn.getFieldsName(), readOnly));
		columnTemplateContext.put("size", deDataLayoutColumn.getColumnSize());

		return columnTemplateContext;
	}

	protected List<Object> createFieldsTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		List<String> fieldNames, boolean readOnly) {

		List<Object> fieldsContext = new ArrayList<>();

		for (String fieldName : fieldNames) {
			DEDataDefinitionField deDataDefinitionField =
				deDataDefinitionFields.get(fieldName);

			fieldsContext.add(
				createFieldTemplateContext(
					httpServletRequest, httpServletResponse,
					deDataDefinitionField, readOnly));
		}

		return fieldsContext;
	}

	protected Map<String, Object> createFieldTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		DEFieldType deFieldType = deFieldTypeTracker.getDEFieldType(
			deDataDefinitionField.getType());

		Map<String, Object> context = new HashMap<>();

		deFieldType.includeContext(
			httpServletRequest, httpServletResponse, context,
			deDataDefinitionField, readOnly);

		return context;
	}

	protected List<Object> createPagesTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		Queue<DEDataLayoutPage> deDataLayoutPages, boolean readOnly) {

		List<Object> pagesContext = new ArrayList<>();

		for (DEDataLayoutPage deDataLayoutPage : deDataLayoutPages) {
			pagesContext.add(
				createPageTemplateContext(
					httpServletRequest, httpServletResponse,
					deDataDefinitionFields, deDataLayoutPage, readOnly));
		}

		return pagesContext;
	}

	protected Map<String, Object> createPageTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		DEDataLayoutPage deDataLayoutPage, boolean readOnly) {

		Map<String, Object> pageTemplateContext = new HashMap<>();

		Map<String, String> title = deDataLayoutPage.getTitle();

		Map<String, String> description = deDataLayoutPage.getDescription();

		String languageId = LanguageUtil.getLanguageId(
			httpServletRequest.getLocale());

		pageTemplateContext.put(
			"description",
			description.getOrDefault(languageId, StringPool.BLANK));

		pageTemplateContext.put(
			"rows",
			createRowsTemplateContext(
				httpServletRequest, httpServletResponse, deDataDefinitionFields,
				deDataLayoutPage.getDEDataLayoutRows(), readOnly));
		pageTemplateContext.put(
			"title", title.getOrDefault(languageId, StringPool.BLANK));

		return pageTemplateContext;
	}

	protected List<Object> createRowsTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		Queue<DEDataLayoutRow> deDataLayoutRows, boolean readOnly) {

		List<Object> rowsContext = new ArrayList<>();

		for (DEDataLayoutRow deDataLayoutRow : deDataLayoutRows) {
			rowsContext.add(
				createRowTemplateContext(
					httpServletRequest, httpServletResponse,
					deDataDefinitionFields, deDataLayoutRow, readOnly));
		}

		return rowsContext;
	}

	protected Map<String, Object> createRowTemplateContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		Map<String, DEDataDefinitionField> deDataDefinitionFields,
		DEDataLayoutRow deDataLayoutRow, boolean readOnly) {

		Map<String, Object> rowTemplateContext = new HashMap<>();

		rowTemplateContext.put(
			"columns",
			createColumnsTemplateContext(
				httpServletRequest, httpServletResponse, deDataDefinitionFields,
				deDataLayoutRow.getDEDataLayoutColumns(), readOnly));

		return rowTemplateContext;
	}

	protected void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataLayout deDataLayout, boolean readOnly) {

		Map<String, DEDataDefinitionField> deDataDefinitionFields =
			DEDataEngineUtil.getDEDataDefinitionFieldsMap(
				deDataLayout.getDEDataDefinition());

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String pathThemeImages = themeDisplay.getPathThemeImages();

		String spriteMap = pathThemeImages.concat("/clay/icons.svg");

		context.put(
			"pages",
			createPagesTemplateContext(
				httpServletRequest, httpServletResponse, deDataDefinitionFields,
				deDataLayout.getDEDataLayoutPages(), readOnly));
		context.put("paginationMode", deDataLayout.getPaginationMode());
		context.put("spritemap", spriteMap);
	}

	@Reference
	protected DEFieldTypeTracker deFieldTypeTracker;

	@Reference
	protected NPMResolver npmResolver;

	@Reference
	protected SoyComponentRenderer soyComponentRenderer;

	private static final String _MODULE_NAME =
		"dynamic-data-mapping-form-builder/metal/js/components/Form" +
			"/FormRenderer.es";

	private static final String _TEMPLATE_NAMESPACE = "FormRenderer.render";

}