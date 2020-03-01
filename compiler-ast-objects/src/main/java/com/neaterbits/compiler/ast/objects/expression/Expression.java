package com.neaterbits.compiler.ast.objects.expression;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

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
