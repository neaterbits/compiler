package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionList;

public abstract class BaseInfixParserListener extends BaseParserListener {

	protected BaseInfixParserListener(ParseLogger logger) {
		super(logger);
	}

	public final void onExpressionBinaryOperator(Context context, Operator operator) {
		final StackExpressionList expressionList = get();
		
		expressionList.addOperator(operator);
	}
}
