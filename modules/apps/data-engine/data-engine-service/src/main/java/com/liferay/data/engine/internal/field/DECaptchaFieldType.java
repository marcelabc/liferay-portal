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

import com.liferay.captcha.taglib.servlet.taglib.CaptchaTag;
import com.liferay.data.engine.field.DEFieldType;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.template.soy.data.SoyDataFactory;
import com.liferay.taglib.servlet.PageContextFactoryUtil;
import com.liferay.taglib.servlet.PipingServletResponse;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "de.data.definition.field.type=captcha",
	service = DEFieldType.class
)
public class DECaptchaFieldType implements DEFieldType {

	@Override
	public void includeContext(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Map<String, Object> context,
		DEDataDefinitionField deDataDefinitionField, boolean readOnly) {

		DEFieldType.super.includeContext(
			httpServletRequest, httpServletResponse, context,
			deDataDefinitionField, readOnly);

		String html = StringPool.BLANK;

		try {
			html = renderCaptchaTag(
				httpServletRequest, httpServletResponse, deDataDefinitionField);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		context.put("html", soyDataFactory.createSoyHTMLData(html));
	}

	protected String renderCaptchaTag(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			DEDataDefinitionField deDataDefinitionField)
		throws Exception {

		CaptchaTag captchaTag = new CaptchaTag();

		captchaTag.setUrl(
			GetterUtil.getString(
				deDataDefinitionField.getCustomProperty("url")));

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		PageContext pageContext = PageContextFactoryUtil.create(
			httpServletRequest,
			new PipingServletResponse(httpServletResponse, unsyncStringWriter));

		captchaTag.setPageContext(pageContext);

		captchaTag.runTag();

		return unsyncStringWriter.toString();
	}

	@Reference
	protected SoyDataFactory soyDataFactory;

	private static final Log _log = LogFactoryUtil.getLog(
		DECaptchaFieldType.class);

}