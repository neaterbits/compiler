package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;

public abstract class ConditionStatement extends Statement {

	protected ConditionStatement(Context context) {
		super(context);
	}
}
