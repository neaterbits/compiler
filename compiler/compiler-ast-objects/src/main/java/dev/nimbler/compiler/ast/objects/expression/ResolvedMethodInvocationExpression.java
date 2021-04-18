package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.method.MethodInvocationType;

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
