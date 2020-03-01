package com.neaterbits.compiler.ast.objects.expression;

import com.neaterbits.compiler.ast.objects.block.FunctionName;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class FunctionCallExpression extends Call<FunctionName> {

	public FunctionCallExpression(Context context, FunctionName function, ParameterList parameters) {
		super(context, function, parameters);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_CALL_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFunctionCall(this, param);
	}
}
