package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.block.MethodName;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.method.MethodInvocationType;
import com.neaterbits.util.parse.context.Context;

public class ResolvedMethodInvocationExpression extends ResolvedCall<MethodName> {

    private final MethodInvocationType type;

	public ResolvedMethodInvocationExpression(
			Context context,
			MethodInvocationType type,
			MethodName callable,
			ParameterList parameters) {
		
		super(context, callable, parameters);

		Objects.requireNonNull(type);

		this.type = type;
	}

	public MethodInvocationType getInvocationType() {
		return type;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.RESOLVED_METHOD_INVOCATION_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onMethodInvocation(this, param);
	}
}
