package com.neaterbits.compiler.ast.objects.expression;

import com.neaterbits.compiler.ast.objects.block.FunctionName;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class FunctionCallExpression extends ResolvedCall<FunctionName> {

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
