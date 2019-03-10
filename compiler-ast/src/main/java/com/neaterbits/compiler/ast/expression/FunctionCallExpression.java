package com.neaterbits.compiler.ast.expression;

import com.neaterbits.compiler.ast.block.FunctionName;
import com.neaterbits.compiler.util.Context;

public final class FunctionCallExpression extends Call<FunctionName> {

	public FunctionCallExpression(Context context, FunctionName function, ParameterList parameters) {
		super(context, function, parameters);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFunctionCall(this, param);
	}
}
