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

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionActionHandler;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFieldAccessor;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionObserver;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionParameterAccessor;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionLexer;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionParser;
import com.liferay.dynamic.data.mapping.expression.model.Expression;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.kernel.util.SetUtil;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author Marcela Cunha
 */
public class FormulaDDMExpressionImpl<T> implements DDMExpression<T> {

	@Override
	public T evaluate() throws DDMExpressionException {
		try {
			FormulaDDMExpressionEvaluatorVisitor
				formulaDDMExpressionEvaluatorVisitor =
					new FormulaDDMExpressionEvaluatorVisitor(
						_variables, _ddmExpressionActionHandler,
						_ddmExpressionFieldAccessor, _ddmExpressionObserver,
						_ddmExpressionParameterAccessor,
						_objectFieldLocalService, _objectDefinitionId);

			return (T)_expressionContext.accept(
				formulaDDMExpressionEvaluatorVisitor);
		}
		catch (Exception exception) {
			throw new DDMExpressionException(exception);
		}
	}

	@Override
	public Expression getModel() {
		return _expressionContext.accept(new DDMExpressionModelVisitor());
	}

	@Override
	public void setVariable(String name, Object value) {
		if (value instanceof Number) {
			value = new BigDecimal(value.toString());
		}

		_variables.put(name, value);
	}

	@Override
	public void setVariables(Map<String, Object> variables) {
		_variables.putAll(variables);
	}

	protected FormulaDDMExpressionImpl(
			long objectDefinitionId, String expression)
		throws DDMExpressionException {

		if ((expression == null) || expression.isEmpty()) {
			throw new IllegalArgumentException();
		}

		_objectDefinitionId = objectDefinitionId;

		DDMExpressionParser ddmExpressionParser = new DDMExpressionParser(
			new CommonTokenStream(
				new DDMExpressionLexer(new ANTLRInputStream(expression))));

		ddmExpressionParser.setErrorHandler(new BailErrorStrategy());

		try {
			_expressionContext = ddmExpressionParser.expression();
		}
		catch (Exception exception) {
			throw new DDMExpressionException.InvalidSyntax(exception);
		}

		ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

		DDMExpressionListener ddmExpressionListener =
			new DDMExpressionListener();

		parseTreeWalker.walk(ddmExpressionListener, _expressionContext);

		Set<String> undefinedFormulaDDMExpressionFunctionNames = new HashSet<>(
			ddmExpressionListener.getFunctionNames());

		undefinedFormulaDDMExpressionFunctionNames.removeAll(
			_formulaDDMExpressionFunctionNames);

		//		if (!undefinedFormulaDDMExpressionFunctionNames.isEmpty()) {
		//			throw new DDMExpressionException.FunctionNotDefined(
		//				undefinedFormulaDDMExpressionFunctionNames);
		//		}
	}

	protected Set<String> getExpressionVariableNames() {
		return _variables.keySet();
	}

	protected void setDDMExpressionActionHandler(
		DDMExpressionActionHandler ddmExpressionActionHandler) {

		_ddmExpressionActionHandler = ddmExpressionActionHandler;
	}

	protected void setDDMExpressionFieldAccessor(
		DDMExpressionFieldAccessor ddmExpressionFieldAccessor) {

		_ddmExpressionFieldAccessor = ddmExpressionFieldAccessor;
	}

	protected void setDDMExpressionObserver(
		DDMExpressionObserver ddmExpressionObserver) {

		_ddmExpressionObserver = ddmExpressionObserver;
	}

	protected void setDDMExpressionParameterAccessor(
		DDMExpressionParameterAccessor ddmExpressionParameterAccessor) {

		_ddmExpressionParameterAccessor = ddmExpressionParameterAccessor;
	}

	protected void setObjectFieldLocalService(
		ObjectFieldLocalService objectFieldLocalService) {

		_objectFieldLocalService = objectFieldLocalService;
	}

	private DDMExpressionActionHandler _ddmExpressionActionHandler;
	private DDMExpressionFieldAccessor _ddmExpressionFieldAccessor;
	private DDMExpressionObserver _ddmExpressionObserver;
	private DDMExpressionParameterAccessor _ddmExpressionParameterAccessor;
	private final DDMExpressionParser.ExpressionContext _expressionContext;
	private final Set<String> _formulaDDMExpressionFunctionNames =
		SetUtil.fromArray("equals", "sum");
	private final long _objectDefinitionId;
	private ObjectFieldLocalService _objectFieldLocalService;
	private final Map<String, Object> _variables = new HashMap<>();

}