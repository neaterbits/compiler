package dev.nimbler.compiler.ast.objects.expression;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;

public abstract class Expression extends BaseASTElement {

	public abstract <T, R> R visit(ExpressionVisitor<T, R> visitor, T param);
	
	// public abstract BaseType getType();
	public abstract TypeReference getType();
	
	public Expression(Context context) {
		super(context);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().toString();
	}
}
