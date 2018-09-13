package com.neaterbits.compiler.common.ast.condition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class Condition extends BaseASTElement {

	public abstract<T, R> R visit(ConditionVisitor<T, R> visitor, T param); 

	public Condition(Context context) {
		super(context);
	}
}
