package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class VariableReference extends BaseASTElement {

	public VariableReference(Context context) {
		super(context);
	}
}
