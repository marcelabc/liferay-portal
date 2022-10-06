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

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionActionHandler;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFieldAccessor;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionObserver;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionParameterAccessor;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionBaseVisitor;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionParser;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author Marcela Cunha
 */
public class FormulaDDMExpressionEvaluatorVisitor
	extends DDMExpressionBaseVisitor<Object> {

	@Override
	public Object visitAdditionExpression(
		@NotNull DDMExpressionParser.AdditionExpressionContext context) {

		Object bigDecimal1 = visitChild(context, 0);
		Object bigDecimal2 = visitChild(context, 2);

		return bigDecimal1;
	}

	@Override
	public Object visitEqualsExpression(
		@NotNull DDMExpressionParser.EqualsExpressionContext context) {

		Object object1 = visitChild(context, 0);
		Object object2 = visitChild(context, 2);

		if ((object1 instanceof Number) && (object2 instanceof Number)) {
			BigDecimal bigDecimal1 = new BigDecimal(object1.toString());
			BigDecimal bigDecimal2 = new BigDecimal(object2.toString());

			return bigDecimal1.compareTo(bigDecimal2) == 0;
		}

		return Objects.equals(object1, object2);
	}

	@Override
	public Object visitExpression(
		@NotNull DDMExpressionParser.ExpressionContext context) {

		DDMExpressionParser.LogicalOrExpressionContext
			logicalOrExpressionContext = context.logicalOrExpression();

		return logicalOrExpressionContext.accept(this);
	}

	@Override
	public Object visitFunctionCallExpression(
		@NotNull DDMExpressionParser.FunctionCallExpressionContext context) {

		Object[] functionParameters = getFunctionParameters(
			context.functionParameters());

		String functionName = getFunctionName(context.functionName);

		if (StringUtil.equals(functionName, "sum")) {
			return true;
		}

		return true;
	}

	@Override
	public Object visitLogicalConstant(
		@NotNull DDMExpressionParser.LogicalConstantContext context) {

		return Boolean.parseBoolean(context.getText());
	}

	@Override
	public Object visitLogicalVariable(
		@NotNull DDMExpressionParser.LogicalVariableContext context) {

		return context.getText();
	}

	protected FormulaDDMExpressionEvaluatorVisitor(
		Map<String, Object> variables,
		DDMExpressionActionHandler ddmExpressionActionHandler,
		DDMExpressionFieldAccessor ddmExpressionFieldAccessor,
		DDMExpressionObserver ddmExpressionObserver,
		DDMExpressionParameterAccessor ddmExpressionParameterAccessor,
		ObjectFieldLocalService objectFieldLocalService,
		long objectDefinitionId) {

		_variables = variables;
		_ddmExpressionActionHandler = ddmExpressionActionHandler;
		_ddmExpressionFieldAccessor = ddmExpressionFieldAccessor;
		_ddmExpressionObserver = ddmExpressionObserver;
		_ddmExpressionParameterAccessor = ddmExpressionParameterAccessor;
		_objectFieldLocalService = objectFieldLocalService;
		_objectDefinitionId = objectDefinitionId;
	}

	protected String getFunctionName(Token functionNameToken) {
		return functionNameToken.getText();
	}

	protected Object[] getFunctionParameters(
		DDMExpressionParser.FunctionParametersContext context) {

		if (context == null) {
			return new Object[0];
		}

		List<Object> parameters = new ArrayList<>();

		for (int i = 0; i < context.getChildCount(); i += 2) {
			Object parameter = visitChild(context, i);

			parameters.add(parameter);
		}

		return parameters.toArray(new Object[0]);
	}

	protected <T> T visitChild(
		ParserRuleContext parserRuleContext, int childIndex) {

		ParseTree parseTree = parserRuleContext.getChild(childIndex);

		return (T)parseTree.accept(this);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FormulaDDMExpressionEvaluatorVisitor.class);

	private final DDMExpressionActionHandler _ddmExpressionActionHandler;
	private final DDMExpressionFieldAccessor _ddmExpressionFieldAccessor;
	private final DDMExpressionObserver _ddmExpressionObserver;
	private final DDMExpressionParameterAccessor
		_ddmExpressionParameterAccessor;
	private Map<String, String> _lambdaVariableExpressionFieldNames;
	private final long _objectDefinitionId;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final Map<String, Object> _variables;

}