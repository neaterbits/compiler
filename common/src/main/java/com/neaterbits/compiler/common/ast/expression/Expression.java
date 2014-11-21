package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.type.BaseType;

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
