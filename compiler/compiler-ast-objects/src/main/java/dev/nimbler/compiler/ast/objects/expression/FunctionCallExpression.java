package dev.nimbler.compiler.ast.objects.expression;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.FunctionName;
import dev.nimbler.compiler.types.ParseTreeElement;

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
