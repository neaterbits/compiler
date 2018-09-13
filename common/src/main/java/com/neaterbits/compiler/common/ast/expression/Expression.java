package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class Expression extends BaseASTElement {

	public abstract <T, R> R visit(ExpressionVisitor<T, R> visitor, T param);
	
	public Expression(Context context) {
		super(context);
	}
}
