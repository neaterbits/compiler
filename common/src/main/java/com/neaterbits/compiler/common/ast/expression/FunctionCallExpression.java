package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.FunctionName;

public final class FunctionCallExpression extends CallExpression<FunctionName> {

	public FunctionCallExpression(Context context, FunctionName function, ParameterList parameters) {
		super(context, function, parameters);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFunctionCall(this, param);
	}
}
