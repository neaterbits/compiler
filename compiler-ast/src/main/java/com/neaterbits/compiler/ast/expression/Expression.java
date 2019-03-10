package com.neaterbits.compiler.ast.expression;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;

public abstract class Expression extends BaseASTElement {

	public abstract <T, R> R visit(ExpressionVisitor<T, R> visitor, T param);
	
	public abstract BaseType getType();
	
	public Expression(Context context) {
		super(context);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().toString();
	}
}
