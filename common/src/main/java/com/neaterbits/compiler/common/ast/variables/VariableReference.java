package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class VariableReference extends BaseASTElement {

	public abstract <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param);
	
	public VariableReference(Context context) {
		super(context);
	}
}
